package delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import delivery.UserDao;
import delivery.Status;

@RestController
public class UsersController {

    private final String AUTH_REQ = "Basic authorization is required";
    private final String USER_EXISTS = "A user with such login already exists";
    private final String INVALID_LOG_PWD = "Invalid login/password";
    private final String ADMIN_CAN = "Only admin can perform this action";
    private final String CANNOT_FIND = "Cannot find user with login: ";

    @Autowired
    UserDao usersDao;

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public Status signUp(HttpServletRequest theRequest,
    		             @RequestParam String firstName, @RequestParam String lastName,
                         @RequestParam String parentName, @RequestParam Date date) {

    	String[] loginPassword = usersDao.getLoginPassword(theRequest);
    	if(loginPassword==null)
    		return new Status(AUTH_REQ);
    	
    	if(usersDao.hasUser(loginPassword[0]))
    		return new Status(USER_EXISTS);
    	
        usersDao.save(new User(loginPassword[0], loginPassword[1], firstName, lastName,
        		               parentName, date, UserRoleEnum.USER));
        return new Status("");
    }
    
    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public Status signIn(HttpServletRequest theRequest,
    		             HttpServletResponse theResponse) {

    	String[] loginPassword = usersDao.getLoginPassword(theRequest);
    	if(loginPassword==null)
    		return new Status(AUTH_REQ);
    				
   	    if(!usersDao.checkPassword(loginPassword[0], loginPassword[1]))
   	    	return new Status(INVALID_LOG_PWD);
    	
	    usersDao.generateToken(theResponse, loginPassword[0]);
	    return new Status("");
    }
    
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<User> products(HttpServletRequest theRequest) {
    	
    	UserRoleEnum role = usersDao.currentRole(theRequest);
    	if(role==UserRoleEnum.ADMIN)
    		return usersDao.findAll();
    	else
    		return null;
    }

    @RequestMapping(value = "/user/role", method = RequestMethod.POST)
	public Status changeRole(HttpServletRequest theRequest,
							 @RequestParam String userLogin,
				  	         @RequestParam UserRoleEnum newRole) {
	
		UserRoleEnum curRole = usersDao.currentRole(theRequest);
		if(curRole==UserRoleEnum.ADMIN)
		{
			if(usersDao.changeRole(userLogin, newRole))
				return new Status("");
			else
				return new Status(CANNOT_FIND + userLogin);
		}
		else
			return new Status(ADMIN_CAN);
	}
}
