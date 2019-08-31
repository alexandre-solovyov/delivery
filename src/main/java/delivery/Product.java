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

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private double price;

    public Product() {}
}
