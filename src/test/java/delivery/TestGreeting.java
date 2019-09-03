
package delivery;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import delivery.Product;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestGreeting {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void defaultIsOK() throws Exception {

        /*this.mockMvc.perform(get("/"))
            //.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("Delivery service is available"));*/
    }
}