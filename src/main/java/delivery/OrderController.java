package delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import javax.servlet.http.*;

@RestController
public class OrderController {
	
    @Autowired
    OrderDao orderDao;
    
    @Autowired
    UserDao usersDao;

    @Autowired
    ProductDao productsDao;
    
    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public List<Order> orders(HttpServletRequest theRequest,
    						  HttpServletResponse theResponse) {
    	
    	try(MyTransaction tr = new MyTransaction()) {
    		
    		User user = usersDao.currentUser(theRequest);
    		if(user!=null) {
    			theResponse.setStatus(HttpServletResponse.SC_OK);
    			return orderDao.findAllForConsumer(user);
    		}
    		else {
    			theResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
    			return null;
    		}
    	}
    }

    @RequestMapping(value = "/order/new", method = RequestMethod.POST)
    public ResponseEntity<Status> createOrder(HttpServletRequest theRequest) {

    	try(MyTransaction tr = new MyTransaction()) {
    		
    		User user = usersDao.currentUser(theRequest);
    		if(user!=null) {
    			orderDao.newCart(user);
    			return new ResponseEntity<>(new Status(""), HttpStatus.OK);
    		}
    		else
    			return new ResponseEntity<>(new Status(Messages.AUTH_REQ), HttpStatus.UNAUTHORIZED);
    	}
    }
    
    @RequestMapping(value = "/order/add", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Status> addToOrder(HttpServletRequest theRequest,
    		                 				 @RequestParam int code, @RequestParam double quantity) {
    	
    	try(MyTransaction tr = new MyTransaction()) {
    		
    		User user = usersDao.currentUser(theRequest);
    		Product product = productsDao.getByCode(code);
    		if(user!=null) {
    			if(product!=null) {
    				orderDao.addToCart(user, product, quantity);
    				return new ResponseEntity<>(new Status(""), HttpStatus.OK);
    			}
    			else
    				return new ResponseEntity<>(new Status(Messages.CANNOT_FIND_PRODUCT + code), HttpStatus.NOT_FOUND);
    		}
    		else
    			return new ResponseEntity<>(new Status(Messages.AUTH_REQ), HttpStatus.UNAUTHORIZED);
    	}
    }
    
    @RequestMapping(value = "/order/cart", method = RequestMethod.GET)
    @ResponseBody
    public OrderJson cartContents(HttpServletRequest theRequest,
    							  HttpServletResponse theResponse) {
    	
    	try(MyTransaction tr = new MyTransaction()) {
    		
    		User user = usersDao.currentUser(theRequest);
    		if(user!=null) {
  				Order cart = orderDao.cart(user, false);
  				theResponse.setStatus(HttpServletResponse.SC_OK);
  				return new OrderJson(cart);
    		}
    		else {
    			theResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
    			return null;
    		}
    	}
    }
    
    @RequestMapping(value = "/order/confirm", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Status> confirm(HttpServletRequest theRequest) {
    	
    	try(MyTransaction tr = new MyTransaction()) {
    		
    		User user = usersDao.currentUser(theRequest);
    		if(user!=null) {
    			orderDao.confirmCart(user);
    			return new ResponseEntity<>(new Status(""), HttpStatus.OK);
    		}
    		else
    			return new ResponseEntity<>(new Status(Messages.AUTH_REQ), HttpStatus.UNAUTHORIZED);
    	}
    }
    
    @RequestMapping(value = "/order/process", method = RequestMethod.GET)
    public List<Order> ordersTo(HttpServletRequest theRequest,
    							HttpServletResponse theResponse) {
    	
    	try(MyTransaction tr = new MyTransaction()) {
    		
    		User user = usersDao.currentUser(theRequest);
    		if(user!=null) {
    			if(user.getRole()==UserRoleEnum.PRODUCER) {
    				theResponse.setStatus(HttpServletResponse.SC_OK);
    				return orderDao.ordersToProcess(user);
    			}
    		}

    		theResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
    		return null;
    	}
    }
    
    @RequestMapping(value = "/orders/changestate", method = RequestMethod.POST)
    public ResponseEntity<Status> changeOrderState(HttpServletRequest theRequest,
    							   				   @RequestParam int theOrderId,
    							   				   @RequestParam OrderStateEnum theState) {
    	
    	try(MyTransaction tr = new MyTransaction()) {
    		
    		User user = usersDao.currentUser(theRequest);
    		if(user!=null) {
    			if(user.getRole()==UserRoleEnum.PRODUCER) {
    				
    				Order order = orderDao.getByIdAndUser(theOrderId, user);
    				if(order!=null) {
    					order.setState(theState);
    					orderDao.update(order);
    					return new ResponseEntity<>(new Status(""), HttpStatus.OK);
    				}
    				else
        				return new ResponseEntity<>(new Status(Messages.CANNOT_FIND_ORDER + " " + user.login() + " " + theOrderId), HttpStatus.NOT_FOUND);
    			}
    			else
    				return new ResponseEntity<>(new Status(Messages.ONLY_PRODUCER_ORDER), HttpStatus.FORBIDDEN);
    		}
    		else
    			return new ResponseEntity<>(new Status(Messages.AUTH_REQ), HttpStatus.UNAUTHORIZED);
    	}
    }
}
