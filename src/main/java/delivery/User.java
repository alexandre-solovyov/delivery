package delivery;

import java.lang.*;
import java.util.*;
import javax.persistence.*;
import org.springframework.stereotype.Component;

@Entity
@Table (name = "users")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "parentname")
    private String parentName;

    @Column(name = "date")
    private Date date;

    @Column(name = "role")
    private String role;

    public User() {}
    public User(String login, String password, String firstName, String lastName,
                String parentName, Date date, String role)
    {
        this.login = login;
        this.login = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.parentName = parentName;
        this.date = date;
        this.role = role;
    }

    public int    getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getRole() { return role; }
}
