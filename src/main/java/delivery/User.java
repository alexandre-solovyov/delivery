package delivery;

import java.util.*;
import javax.persistence.*;

import org.apache.commons.codec.digest.DigestUtils;

@Entity
@Table (name = "users", indexes = {
        @Index(columnList = "id", name = "user_id_index"),
        @Index(columnList = "login", name = "user_login_index")})
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
    private UserRoleEnum role;

    public User() {}
    public User(String login, String password, String firstName, String lastName,
                String parentName, Date date, UserRoleEnum role)
    {
        this.login = login;
        this.password = encode(password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.parentName = parentName;
        this.date = date;
        this.role = role;
    }
    
    public static String encode(String text) {
    	return DigestUtils.md5Hex(text);
    }
    public int    getId() { return id; }
    
    public String getFirstName() { return firstName; }
    public void   setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void   setLastName(String lastName) { this.lastName = lastName; }

    public String getParentName() { return parentName; }
    public void   setParentName(String parentName) { this.parentName = parentName; }
    
    public UserRoleEnum getRole() { return role; }
    public void setRole(UserRoleEnum newRole) { role = newRole; }
    
    public String login() { return login; }
    public String encryptedPassword() { return password; }
    
    public void setPassword(String password) {
    	this.password = encode(password);
    }
}
