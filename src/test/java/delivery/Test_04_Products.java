
package delivery;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.TreeMap;

import org.junit.runner.*;
import org.junit.runners.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Test_04_Products extends BasicTest {

	private final TreeMap<String, Object> empty = new TreeMap<>();
	
    @Test
    public void defaultProductsList() {

    	cleanCookies();
    	
        assertEquals("200 []", GET("/products"));
    }

    @Test
    public void newProduct() {
    	
    	cleanCookies();
    	
    	TreeMap<String, Object> params = new TreeMap<>();
    	params.put("code", 1234);
    	params.put("name", "Notebook HP");
    	params.put("price", 15000);
    	
    	assertEquals("202 {message: }",
    			POST("/signin", empty, "alex", "alex"));    	
        assertEquals("201 {message: }",
        		POST("/product/new", params, "alex", "alex"));
        assertEquals("200 [{code: 1234, name: Notebook HP, price: 15000.0}]",
        		GET("/products"));
    }

    @Test
    public void newProductInvalidData() {
    	
    	cleanCookies();
    	
    	// invalid parameters
    	
    	TreeMap<String, Object> empty = new TreeMap<>();
    	TreeMap<String, Object> params = new TreeMap<>();
    	params.put("code", 12345);
    	params.put("name", "Notebook HP");
    	params.put("price", "g"); // the price is not a number 
        assertEquals("400 {message: Failed to convert value of type 'java.lang.String' to required type 'double'; nested exception is java.lang.NumberFormatException:  For input string:  \\g\\}",
        		POST("/product/new", params, "", ""));
        
        params.remove("price"); // without the price argument
        assertEquals("400 {message: Required double parameter 'price' is not present}",
        		POST("/product/new", params, "", ""));
        
        params.put("price", "0");
        assertEquals(String.format("401 {message: %s}", Messages.AUTH_REQ),
        		POST("/product/new", params, "", ""));

        params.put("price", "0"); // zero, but with authorization
        assertEquals("202 {message: }",
        		POST("/signin", empty, "alex", "alex"));
        assertEquals(String.format("400 {message: %s}", Messages.POS_PRICE),
        		POST("/product/new", params, "", ""));
    }
    
    @Test
    public void newSameProduct() {
    	
    	cleanCookies();
    	
    	assertEquals("202 {message: }",
    			POST("/signin", empty, "alex", "alex"));
    	
        // the same product twice
    	TreeMap<String, Object> params = new TreeMap<>();
    	params.put("code", 1234);
    	params.put("name", "Notebook HP");
    	params.put("price", 15000);
        assertEquals(String.format("409 {message: %s}", Messages.PRODUCT_EXISTS),
        		POST("/product/new", params, "alex", "alex"));
        assertEquals("200 [{code: 1234, name: Notebook HP, price: 15000.0}]",
        		GET("/products"));
    }
    
    @Test
    public void product_ChangePrice() {
    	
    	cleanCookies();

    	assertEquals("202 {message: }",
    			POST("/signin", empty, "alex", "alex"));

    	TreeMap<String, Object> params = new TreeMap<>();
    	params.put("code", 1234);
    	params.put("name", "Notebook HP Pavillion");

    	assertEquals("202 {message: }",
    			PATCH("/product/update", params, "", ""));
        assertEquals("200 [{code: 1234, name: Notebook HP Pavillion, price: 15000.0}]",
        		GET("/products"));
        
        params.clear();
    	params.put("code", 1234);
    	params.put("price", 25000);

    	assertEquals("202 {message: }",
    			PATCH("/product/update", params, "", ""));
        assertEquals("200 [{code: 1234, name: Notebook HP Pavillion, price: 25000.0}]",
        		GET("/products"));    	
    }
}
