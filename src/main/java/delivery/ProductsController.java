package delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import delivery.ProductDao;

@RestController
public class ProductsController {

    @Autowired
    ProductDao productsDao;

    @RequestMapping("/products")
    public List<Product> products() {
        return productsDao.findAll();
    }
}
