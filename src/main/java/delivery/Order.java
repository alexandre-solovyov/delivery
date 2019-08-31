package delivery;

import java.lang.*;
import javax.persistence.*;
import org.springframework.stereotype.Component;

@Entity
@Table (name = "orders")
class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public Order() {}

    public int getId() { return id; }
}
