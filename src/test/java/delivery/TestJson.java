
package delivery;

import static org.junit.Assert.assertEquals;
import java.util.Date;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestJson {
    
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
