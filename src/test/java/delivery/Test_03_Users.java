
package delivery;

import java.util.TreeMap;

import org.junit.*;
import static org.junit.Assert.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//TODO: correct HTTP codes

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Test_03_Users extends BasicTest {
    
    @Test
    public void defaultUsersList() {

    	cleanCookies();
    	
        assertEquals("403 ", GET("/users"));
    }

    @Test
    public void newUser() {

    	cleanCookies();
    	
    	TreeMap<String, Object> empty = new TreeMap<>();
    	TreeMap<String, Object> params = new TreeMap<>();
    	params.put("firstName", "alex");
    	params.put("lastName", "solovyov");
    	params.put("parentName", "g");
    	params.put("date", "31/08/2019");
    	
        assertEquals("201 {message: }",
        		POST("/signup", params, "alex", "alex"));
        assertEquals("202 {message: }",
        		POST("/signin", empty, "alex", "alex"));
        assertEquals("200 [{id: 1, firstName: alex, lastName: solovyov, role: ADMIN}]",
        		GET("/users"));
    }
    
    @Test
    public void newUserInvalidData() {

    	cleanCookies();
    	
    	TreeMap<String, Object> params = new TreeMap<>();
    	params.put("firstName", "test");
    	params.put("lastName", "test");

    	// without some parameters
        assertEquals("400 {message: Required String parameter 'parentName' is not present}",
        		POST("/signup", params, "test", "test"));
        
    	params.put("parentName", "g");
    	params.put("date", "31/08/2019");
    	
    	// without password
        assertEquals(String.format("400 {message: %s}", Messages.AUTH_REQ),
        		POST("/signup", params, "test", ""));

        // without login
        assertEquals(String.format("400 {message: %s}", Messages.AUTH_REQ),
        		POST("/signup", params, "", "test"));
        
        // correct sign up
        assertEquals("201 {message: }",
        		POST("/signup", params, "test", "test"));
        
        // second time the same user
        assertEquals(String.format("409 {message: %s}", Messages.USER_EXISTS),
        		POST("/signup", params, "test", "test"));
    }

    @Test
    public void invalidSignsIn() {
    	
    	cleanCookies();
    	
    	TreeMap<String, Object> empty = new TreeMap<>();
    	assertEquals(String.format("401 {message: %s}", Messages.INVALID_LOG_PWD),
    			POST("/signin", empty, "alex", "alex2"));
    	assertEquals(String.format("401 {message: %s}", Messages.INVALID_LOG_PWD),
    			POST("/signin", empty, "john", "john"));
    }
}
