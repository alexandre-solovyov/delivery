package delivery;

import javax.persistence.*;

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
    public Product(int theCode, String theName, double thePrice, User theProducer)
    {
        this.code = theCode;
        this.name = theName;
        this.price = thePrice;
        this.producer = theProducer;
    }

    public int    getCode() { return code; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public User producer() { return producer; }
    public void setProducer(User producer) { this.producer = producer; }
}
