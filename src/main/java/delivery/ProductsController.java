package delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import delivery.ProductDao;
import delivery.Status;

@RestController
public class ProductsController {

    @Autowired
    ProductDao productsDao;

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public List<Product> products() {
        return productsDao.findAll();
    }

    @RequestMapping(value = "/newproduct", method = RequestMethod.POST)
    public Status newProduct(@RequestParam int code, @RequestParam String name, @RequestParam double price) {

        if(name.length()==0)
            return new Status("The name must not be empty");
        if(code <= 0)
            return new Status("The code must be positive");
        if(price <= 0)
            return new Status("The price must be positive");

        productsDao.save(new Product(code, name, price));
        return new Status("");        
    }
}
