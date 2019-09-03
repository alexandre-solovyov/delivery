
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
    public void createUserIsOK() throws Exception {

    	TreeMap<String, Object> empty = new TreeMap<>();
    	TreeMap<String, Object> params = new TreeMap<>();
    	params.put("firstName", "alex");
    	params.put("lastName", "solovyov");
    	params.put("parentName", "g");
    	params.put("date", "31/08/2019");
    	
        assertEquals("200 {result: OK, message: }", POST("/signup", params, "alex", "alex"));
        assertEquals("200 {result: OK, message: }", POST("/signin", empty, "alex", "alex"));
        assertEquals("200 [{id: 1, firstName: alex, lastName: solovyov, role: ADMIN}]", GET("/users"));
    }

}
