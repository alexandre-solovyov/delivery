package delivery;

import java.lang.*;
import javax.persistence.*;
import org.springframework.stereotype.Component;

@Entity
@Table (name = "products")
class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "code")
    private int code;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producer_id")
    private User producer;

    public Product() {}
    public Product(int theCode, String theName, double thePrice)
    {
        this.code = theCode;
        this.name = theName;
        this.price = thePrice;
    }

    public int    getCode() { return code; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public User   getProducer() { return producer; }
}
