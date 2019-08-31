package delivery;

import java.lang.*;
import javax.persistence.*;
import org.springframework.stereotype.Component;

@Entity
@Table (name = "users")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    public User() {}
    public User(String theName)
    {
        this.name = theName;
    }

    public int    getId() { return id; }
    public String getName() { return name; }
}
