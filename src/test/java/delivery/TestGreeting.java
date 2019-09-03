
package delivery;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestGreeting extends BasicTest {
    
    @Test
    public void greetingIsOK() throws Exception {

        assertEquals("200 {result: Delivery service is available}", GET("/"));
    }
}
