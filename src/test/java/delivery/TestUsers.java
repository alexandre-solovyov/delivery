
package delivery;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.TreeMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestUsers extends BasicTest {
    
    @Test
    public void defaultUsersListIsOK() throws Exception {

        assertEquals("200 ", GET("/users"));
    }

    @Test
    public void createUserWithInvalidData() throws Exception {

    	TreeMap<String, Object> params = new TreeMap<>();
    	params.put("firstName", "test");
    	params.put("lastName", "test");

    	// without some parameters
        assertEquals("400 ", POST("/signup", params, "test", "test"));
        
    	params.put("parentName", "g");
    	params.put("date", "31/08/2019");
    	
    	// without password
        assertEquals("200 {result: Failed, message: Basic authorization is required}", POST("/signup", params, "test", ""));

        // without login
        assertEquals("200 {result: Failed, message: Basic authorization is required}", POST("/signup", params, "", "test"));
        
        // correct sign up
        assertEquals("200 {result: OK, message: }", POST("/signup", params, "test", "test"));
        
        // second time the same user
        assertEquals("200 {result: Failed, message: A user with such login already exists}", POST("/signup", params, "test", "test"));
    }
    
    @Test
    public void createUser() throws Exception {

    	TreeMap<String, Object> empty = new TreeMap<>();
    	TreeMap<String, Object> params = new TreeMap<>();
    	params.put("firstName", "alex");
    	params.put("lastName", "solovyov");
    	params.put("parentName", "g");
    	params.put("date", "31/08/2019");
    	
        assertEquals("200 {result: OK, message: }", POST("/signup", params, "alex", "alex"));
        assertEquals("200 {result: OK, message: }", POST("/signin", empty, "alex", "alex"));
        assertEquals("200 [{id: 1, firstName: test, lastName: test, role: USER}, {id: 2, firstName: alex, lastName: solovyov, role: ADMIN}]", GET("/users"));
    }

    @Test
    public void invalidSignFails() throws Exception {
    	
    	TreeMap<String, Object> empty = new TreeMap<>();
    	assertEquals("200 {result: Failed, message: Invalid login/password}", POST("/signin", empty, "alex", "alex2"));
    	assertEquals("200 {result: Failed, message: Invalid login/password}", POST("/signin", empty, "john", "john"));
    }
}
