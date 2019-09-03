package delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.*;
import delivery.ProductDao;
import delivery.Status;

@RestController
public class ProductsController {

	private static final String NOT_PRODUCER = "The user is not producer: ";
	
    @Autowired
    ProductDao productsDao;

    @Autowired
    UserDao usersDao;
    
    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public List<Product> products(MyTransaction tr) {
        return productsDao.findAll(tr);
    }

    @RequestMapping(value = "/product/new", method = RequestMethod.POST)
    public Status createProduct(@RequestParam int code, @RequestParam String name, @RequestParam double price) {

        if(name.length()==0)
            return new Status("The name must not be empty");
        if(code <= 0)
            return new Status("The code must be positive");
        if(price <= 0)
            return new Status("The price must be positive");

        try(MyTransaction tr = new MyTransaction()) {
        	productsDao.save(new Product(code, name, price, null), tr);
        }
        return new Status("");        
    }
    
    @RequestMapping(value = "/product/upload", method = RequestMethod.POST)
    @ResponseBody
    public Status uploadProducts(MultipartHttpServletRequest theRequest,
    							 @RequestParam String producerLogin) throws Exception {
    	
    	try(MyTransaction tr = new MyTransaction()) {
    		
    		Iterator<String> iterator = theRequest.getFileNames();
    		MultipartFile multiFile = theRequest.getFile(iterator.next());

    		// just to show that we have actually received the file
            //System.out.println("Uploaded file length: " + multiFile.getBytes().length);
            //System.out.println("Uploaded file type: " + multiFile.getContentType());
            
            String fileName = multiFile.getOriginalFilename();
            //System.out.println("Uploaded file name: " + fileName);
            
            String filePath = theRequest.getServletContext().getRealPath("/");
            //System.out.println("Uploaded file path: " + filePath);
            
            byte[] bytes = multiFile.getBytes();
            User producer = usersDao.getUserByLogin(producerLogin, tr);
            //System.out.println(producerLogin + " " + producer);
            if(producer==null || producer.getRole()!=UserRoleEnum.PRODUCER) {
            	tr.rollback();
            	return new Status(NOT_PRODUCER + producerLogin);
            }
            
            String sheetName = ""; // it means the first
            productsDao.readFromExcel(bytes, sheetName, producer, tr);
        }
        return new Status("");
    }
}
