
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
public class TestProducts {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createProductIsOK() throws Exception {

        /*this.mockMvc.perform(post("/newproduct").param("name", "pizza").param("price", "100"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("OK"))
            .andExpect(jsonPath("$.message").value(""));

        this.mockMvc.perform(post("/newproduct").param("name", "").param("price", "100"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("Failed"))
            .andExpect(jsonPath("$.message").value("The name must not be empty"));

        this.mockMvc.perform(post("/newproduct").param("name", "pizza").param("price", "0"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("Failed"))
            .andExpect(jsonPath("$.message").value("The price must be positive"));

        this.mockMvc.perform(get("/products"))
            .andExpect(jsonPath("$[0].name").value("pizza"))
            .andExpect(jsonPath("$[0].price").value(100.0));*/
    }
}
