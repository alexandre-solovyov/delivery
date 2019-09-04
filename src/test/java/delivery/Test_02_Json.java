
package delivery;

import java.util.Date;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.*;
import static org.junit.Assert.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Test_02_Json {
    
	private static ObjectMapper mapper = new ObjectMapper();
	
    @Test
    public void orderItemJsonIsOK() throws JsonProcessingException {

    	User consumer = new User("alex", "alex", "Alexander", "", "", new Date(), UserRoleEnum.USER);
    	Product notebook = new Product(12345, "Notebook", 35000, null);
    	Product phone = new Product(567, "Samsung Galaxy", 15000, null);
    	
    	Order order = new Order(consumer);
    	order.add(notebook, 3.0);
    	order.add(phone, 2.0);
    	
    	OrderJson jOrder = new OrderJson(order);

    	String json = mapper.writeValueAsString(jOrder);
    	json = BasicTest.postprocess(json);
    	
  		assertEquals("{items: [{product: Notebook, quantity: 3.0, price: 35000.0}, {product: Samsung Galaxy, quantity: 2.0, price: 15000.0}], price: 135000.0}", json);
    }
}
