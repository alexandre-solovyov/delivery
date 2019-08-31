package delivery;

import java.lang.*;
import org.springframework.stereotype.Component;

@Component
class Product {
    private String name;
    private double price;

    public Product()
    {
    }
    static public Product create(String theName, double thePrice)
    {
        Product p = new Product();
        p.name = theName;
        p.price = thePrice;
        return p;
    }
    public String getName() { return name; }
    public double getPrice() { return price; }
}
