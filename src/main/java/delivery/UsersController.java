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

    public static final String AUTH_REQ = "Basic authorization is required";
    private static final String USER_EXISTS = "A user with such login already exists";
    private static final String INVALID_LOG_PWD = "Invalid login/password";
    private static final String ADMIN_CAN = "Only admin can perform this action";
    private static final String CANNOT_FIND = "Cannot find user with login: ";

    @Autowired
    UserDao usersDao;

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public Status signUp(HttpServletRequest theRequest,
    		             @RequestParam String firstName, @RequestParam String lastName,
                         @RequestParam String parentName, @RequestParam Date date) {

    	try(MyTransaction tr = new MyTransaction()) {
    		
    		String[] loginPassword = usersDao.getLoginPassword(theRequest);
    		if(loginPassword==null) {
    			tr.rollback();
    			return new Status(AUTH_REQ);
    		}
    	
    		if(usersDao.getUserByLogin(loginPassword[0], tr) != null) {
    			tr.rollback();
    			return new Status(USER_EXISTS);
    		}
    	
    		usersDao.save(new User(loginPassword[0], loginPassword[1], firstName, lastName,
    					  parentName, date, UserRoleEnum.USER), tr);
    		return new Status("");
    	}
    }
    
    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public Status signIn(HttpServletRequest theRequest,
    		             HttpServletResponse theResponse) {

    	try(MyTransaction tr = new MyTransaction()) {
    		
    		String[] loginPassword = usersDao.getLoginPassword(theRequest);
    		if(loginPassword==null)
    			return new Status(AUTH_REQ);
    				
    		if(!usersDao.checkPassword(loginPassword[0], loginPassword[1], tr))
    			return new Status(INVALID_LOG_PWD);
    	
    		usersDao.generateToken(theResponse, loginPassword[0], tr);
    		return new Status("");
    	}
    }
    
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<User> users(HttpServletRequest theRequest) {
    	
    	try(MyTransaction tr = new MyTransaction()) {
    		
    		User user = usersDao.currentUser(theRequest, tr);
    		if(user!=null)
    		{
    			UserRoleEnum role = user.getRole();
    			if(role==UserRoleEnum.ADMIN)
    				return usersDao.findAll(tr);
    		}
    		return null;
    	}
    }

    @RequestMapping(value = "/user/role", method = RequestMethod.POST)
	public Status changeRole(HttpServletRequest theRequest,
							 @RequestParam String userLogin,
				  	         @RequestParam UserRoleEnum newRole) {
	
    	try(MyTransaction tr = new MyTransaction()) {
    		
    		User user = usersDao.currentUser(theRequest, tr);
    		if(user!=null)
    		{
    			UserRoleEnum curRole = user.getRole();
    			System.out.println(user.login() + " " + curRole);
    			if(curRole==UserRoleEnum.ADMIN)
    			{
    				if(usersDao.changeRole(userLogin, newRole, tr))
    					return new Status("");
    				else
    					return new Status(CANNOT_FIND + userLogin);
    			}
    			else
    				return new Status(ADMIN_CAN);
    		}
    		else
    			return new Status(AUTH_REQ);
    	}
	}
}
