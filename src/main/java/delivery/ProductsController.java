package delivery;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import delivery.ProductDao;

@RestController
public class ProductsController {

    private List<Product> products;

    public ProductsController()
    {
        products = new ArrayList();
        //products.add(Product.create("pizza", 450));
        //products.add(Product.create("cola", 70));
    }

    @RequestMapping("/products")
    public List<Product> products() { return products; }
}


