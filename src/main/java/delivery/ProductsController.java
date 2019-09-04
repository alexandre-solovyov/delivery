package delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ProductsController {
	
    @Autowired
    ProductDao productsDao;

    @Autowired
    UserDao usersDao;
    
    public boolean canChangeProducts(UserRoleEnum role) {
    	return role==UserRoleEnum.PRODUCER || role==UserRoleEnum.ADMIN;
    }
    
    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public List<Product> products() {
    	
    	try(MyTransaction tr = new MyTransaction()) {
    		return productsDao.findAll();
    	}
    }

    @RequestMapping(value = "/product/new", method = RequestMethod.POST)
    public ResponseEntity<Status> createProduct(HttpServletRequest theRequest,
    		                    				@RequestParam int code,
    		                    				@RequestParam String name,
    		                    				@RequestParam double price) {

    	User current = usersDao.currentUser(theRequest);
    	if(current==null)
    		return new ResponseEntity<>(new Status(Messages.AUTH_REQ), HttpStatus.UNAUTHORIZED);
    	
    	if(!canChangeProducts(current.getRole()))
    		return new ResponseEntity<>(new Status(Messages.ONLY_PRODUCER_PRODUCT), HttpStatus.FORBIDDEN);
    	
    	Product product = productsDao.getByCode(code);
    	if(product!=null)
    		return new ResponseEntity<>(new Status(Messages.PRODUCT_EXISTS), HttpStatus.CONFLICT);

        if(name.length()==0)
            return new ResponseEntity<>(new Status(Messages.NON_EMPTY_NAME), HttpStatus.BAD_REQUEST);
        if(code <= 0)
            return new ResponseEntity<>(new Status(Messages.POS_CODE), HttpStatus.BAD_REQUEST);
        if(price <= 0)
            return new ResponseEntity<>(new Status(Messages.POS_PRICE), HttpStatus.BAD_REQUEST);

        try(MyTransaction tr = new MyTransaction()) {
        	productsDao.save(new Product(code, name, price, null));
        }
        return new ResponseEntity<>(new Status(""), HttpStatus.CREATED);        
    }
    
    @RequestMapping(value = "/product/update", method = RequestMethod.PATCH)
    public ResponseEntity<Status> updateProduct(HttpServletRequest theRequest,
    		                    				@RequestParam int code,
    		                    				@RequestParam(defaultValue = "") String name,
    		                    				@RequestParam(defaultValue = "-1.0") double price) {
    	
    	User current = usersDao.currentUser(theRequest);
    	if(current==null)
    		return new ResponseEntity<>(new Status(Messages.AUTH_REQ), HttpStatus.UNAUTHORIZED);
    	
    	if(!canChangeProducts(current.getRole()))
    		return new ResponseEntity<>(new Status(Messages.ONLY_PRODUCER_PRODUCT), HttpStatus.FORBIDDEN);
    	
    	Product product = productsDao.getByCode(code);
    	if(product==null)
    		return new ResponseEntity<>(new Status(Messages.CANNOT_FIND_PRODUCT + code), HttpStatus.NOT_FOUND);
    	
    	try(MyTransaction tr = new MyTransaction()) {
    		if(name.length() > 0)
    			product.setName(name);
    		if(price > 0.0)
    			product.setPrice(price);
    		productsDao.update(product);
    	}
    	
    	return new ResponseEntity<>(new Status(""), HttpStatus.ACCEPTED);
    }
    
    @RequestMapping(value = "/product/upload", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Status> uploadProducts(MultipartHttpServletRequest theRequest,
    							 				 @RequestParam String producerLogin) throws Exception {
    	
    	try(MyTransaction tr = new MyTransaction()) {
    		
    		Iterator<String> iterator = theRequest.getFileNames();
    		MultipartFile multiFile = theRequest.getFile(iterator.next());

    		// just to show that we have actually received the file
            //System.out.println("Uploaded file length: " + multiFile.getBytes().length);
            //System.out.println("Uploaded file type: " + multiFile.getContentType());
            
            //String fileName = multiFile.getOriginalFilename();
            //System.out.println("Uploaded file name: " + fileName);
            
            //String filePath = theRequest.getServletContext().getRealPath("/");
            //System.out.println("Uploaded file path: " + filePath);
            
            byte[] bytes = multiFile.getBytes();
            User producer = usersDao.getUserByLogin(producerLogin);
            //System.out.println(producerLogin + " " + producer);
            if(producer==null || !canChangeProducts(producer.getRole())) {
            	tr.rollback();
            	return new ResponseEntity<>(new Status(Messages.ONLY_PRODUCER_PRODUCT), HttpStatus.FORBIDDEN);
            }
            
            String sheetName = ""; // it means the first sheet
            productsDao.readFromExcel(bytes, sheetName, producer, tr);
        }
        return new ResponseEntity<>(new Status(""), HttpStatus.OK);
    }
    
    
    //TODO:
    //new product does not implement new products with the same code
    //implement DELETE, PUT (new parameters), PATCH (for parameters, price etc)
    /* create indices (for code):
     * @Entity
		@Table(name = "users", indexes = {
        @Index(columnList = "id", name = "user_id_hidx"),
        @Index(columnList = "current_city", name = "cbplayer_current_city_hidx")
     */
    
    // statuses to messages class
    
}
