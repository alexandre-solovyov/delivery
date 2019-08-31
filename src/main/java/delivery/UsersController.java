package delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import delivery.UserDao;
import delivery.Status;

@RestController
public class UsersController {

    @Autowired
    UserDao usersDao;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<User> products() {
        return usersDao.findAll();
    }

    @RequestMapping(value = "/newuser", method = RequestMethod.POST)
    public Status newUser(@RequestParam String login, @RequestParam String password,
                          @RequestParam String firstName, @RequestParam String lastName,
                          @RequestParam String parentName, @RequestParam Date date,
                          @RequestParam String role) {

        usersDao.save(new User(login, password, firstName, lastName, parentName, date, role));
        return new Status("");        
    }
}
