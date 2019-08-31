package delivery;

import java.lang.*;
import javax.persistence.*;
import org.springframework.stereotype.Component;

@Component
@Entity
@Table (name = "products")
class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private double price;

    public Product() {}
    public Product(String theName, double thePrice)
    {
        this.name = theName;
        this.price = thePrice;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
}
