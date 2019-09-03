package delivery;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import delivery.OrderDao;
import delivery.Status;
import delivery.MyTransaction;

@RestController
public class OrderController {

	private static final String PRODUCER_CAN = "Only producer can change state of order";
	private static final String CANNOT_FIND_ORDER = "Cannot find order: ";
	private static final String CANNOT_FIND_PRODUCT = "Cannot find product with code: ";
	
    @Autowired
    OrderDao orderDao;
    
    @Autowired
    UserDao usersDao;

    @Autowired
    ProductDao productsDao;
    
    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public List<Order> orders(HttpServletRequest theRequest) {
    	
    	try(MyTransaction tr = new MyTransaction()) {
    		
    		User user = usersDao.currentUser(theRequest, tr);
    		if(user!=null)
    			return orderDao.findAllForConsumer(user, tr);
    		else
    			return null;
    	}
    }

    @RequestMapping(value = "/order/new", method = RequestMethod.POST)
    public Status createOrder(HttpServletRequest theRequest) {

    	try(MyTransaction tr = new MyTransaction()) {
    		
    		User user = usersDao.currentUser(theRequest, tr);
    		if(user!=null) {
    			orderDao.newCart(user, tr);
    			return new Status("");
    		}
    		else
    			return new Status(UsersController.AUTH_REQ);
    	}
    }
    
    @RequestMapping(value = "/order/add", method = RequestMethod.POST)
    @ResponseBody
    public Status addToOrder(HttpServletRequest theRequest,
    		                 @RequestParam int code, @RequestParam double quantity) {
    	
    	try(MyTransaction tr = new MyTransaction()) {
    		
    		User user = usersDao.currentUser(theRequest, tr);
    		Product product = productsDao.getByCode(code, tr);
    		if(user!=null) {
    			if(product!=null) {
    				orderDao.addToCart(user, product, quantity, tr);
    				return new Status("");
    			}
    			else
    				return new Status(CANNOT_FIND_PRODUCT + code);
    		}
    		else
    			return new Status(UsersController.AUTH_REQ);
    	}
    }
    
    @RequestMapping(value = "/order/cart", method = RequestMethod.GET)
    @ResponseBody
    public OrderJson cartContents(HttpServletRequest theRequest) {
    	
    	try(MyTransaction tr = new MyTransaction()) {
    		
    		User user = usersDao.currentUser(theRequest, tr);
    		if(user!=null) {
  				Order cart = orderDao.cart(user, false, tr);
  				return new OrderJson(cart);
    		}
    		else
    			return null;
    	}
    }
    
    @RequestMapping(value = "/order/confirm", method = RequestMethod.POST)
    @ResponseBody
    public Status confirm(HttpServletRequest theRequest) {
    	
    	try(MyTransaction tr = new MyTransaction()) {
    		
    		User user = usersDao.currentUser(theRequest, tr);
    		if(user!=null) {
    			orderDao.confirmCart(user, tr);
    			return new Status("");
    		}
    		else
    			return new Status(UsersController.AUTH_REQ);
    	}
    }
    
    @RequestMapping(value = "/orders/process", method = RequestMethod.GET)
    public List<Order> ordersTo(HttpServletRequest theRequest) {
    	
    	try(MyTransaction tr = new MyTransaction()) {
    		
    		User user = usersDao.currentUser(theRequest, tr);
    		if(user!=null) {
    			if(user.getRole()==UserRoleEnum.PRODUCER) {
    				return orderDao.ordersToProcess(user, tr);
    			}
    		}

    		return null;
    	}
    }
    
    @RequestMapping(value = "/orders/changestate", method = RequestMethod.POST)
    public Status changeOrderState(HttpServletRequest theRequest,
    							   @RequestParam int theOrderId,
    							   @RequestParam OrderStateEnum theState) {
    	
    	try(MyTransaction tr = new MyTransaction()) {
    		
    		User user = usersDao.currentUser(theRequest, tr);
    		if(user!=null) {
    			if(user.getRole()==UserRoleEnum.PRODUCER) {
    				
    				Order order = orderDao.getByIdAndUser(theOrderId, user, tr);
    				if(order!=null) {
    				}
    				else
        				return new Status(CANNOT_FIND_ORDER + " " + user.login() + " " + theOrderId);
    			}
    			else
    				return new Status(PRODUCER_CAN);
    		}
    		else
    			return new Status(UsersController.AUTH_REQ);
    	}
    	
    	return new Status("");
    }
}
