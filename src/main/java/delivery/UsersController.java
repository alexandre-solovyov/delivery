package delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class UsersController {

    @Autowired
    UserDao usersDao;

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<Status> signUp(HttpServletRequest theRequest,
    		             				 @RequestParam String firstName, @RequestParam String lastName,
    		             				 @RequestParam String parentName, @RequestParam Date date) {

    	try(MyTransaction tr = new MyTransaction()) {
    		
    		String[] loginPassword = usersDao.getLoginPassword(theRequest);
    		if(loginPassword==null) {
    			tr.rollback();
    			return new ResponseEntity<>(new Status(Messages.AUTH_REQ), HttpStatus.BAD_REQUEST);
    		}
    	
    		if(usersDao.getUserByLogin(loginPassword[0]) != null) {
    			tr.rollback();
    			return new ResponseEntity<>(new Status(Messages.USER_EXISTS), HttpStatus.CONFLICT);
    		}
    	
    		UserRoleEnum role = UserRoleEnum.USER;
    		//Some hack to create a first ADMIN:
    		if(loginPassword[0].contentEquals("alex") && loginPassword[1].contentEquals("alex"))
    			role = UserRoleEnum.ADMIN;
    		
    		User user = new User(loginPassword[0], loginPassword[1], firstName, lastName,
					  			 parentName, date, role);
    		usersDao.save(user);
    		return new ResponseEntity<>(new Status(""), HttpStatus.CREATED);
    	}
    }
    
    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public ResponseEntity<Status> signIn(HttpServletRequest theRequest,
    		             				 HttpServletResponse theResponse) {

    	try(MyTransaction tr = new MyTransaction()) {
    		
    		String[] loginPassword = usersDao.getLoginPassword(theRequest);
    		if(loginPassword==null)
    			return new ResponseEntity<>(new Status(Messages.AUTH_REQ), HttpStatus.UNAUTHORIZED);
    				
    		if(!usersDao.checkPassword(loginPassword[0], loginPassword[1]))
    			return new ResponseEntity<>(new Status(Messages.INVALID_LOG_PWD), HttpStatus.UNAUTHORIZED);
    	
    		usersDao.generateToken(theResponse, loginPassword[0]);
    		return new ResponseEntity<>(new Status(""), HttpStatus.ACCEPTED);
    	}
    }
    
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<User> users(HttpServletRequest theRequest,
    						HttpServletResponse theResponse) {
    	
    	try(MyTransaction tr = new MyTransaction()) {
    		
    		//System.out.println("Users are requested");
    		
    		User user = usersDao.currentUser(theRequest);
    		if(user!=null)
    		{
    			UserRoleEnum role = user.getRole();
    			//System.out.println("User: " + user.login());
    			//System.out.println("Role: " + user.getRole());
    			if(role==UserRoleEnum.ADMIN) {
    				theResponse.setStatus(HttpServletResponse.SC_OK);
    				return usersDao.findAll();
    			}
    		}
    		theResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
    		return null;
    	}
    }

    @RequestMapping(value = "/user/role", method = RequestMethod.POST)
	public ResponseEntity<Status> changeRole(HttpServletRequest theRequest,
							 				 @RequestParam String userLogin,
							 				 @RequestParam UserRoleEnum newRole) {
	
    	try(MyTransaction tr = new MyTransaction()) {
    		
    		User user = usersDao.currentUser(theRequest);
    		if(user!=null)
    		{
    			UserRoleEnum curRole = user.getRole();
    			//System.out.println(user.login() + " " + curRole);
    			if(curRole==UserRoleEnum.ADMIN)
    			{
    				if(usersDao.changeRole(userLogin, newRole))
    					return new ResponseEntity<>(new Status(""), HttpStatus.OK);
    				else
    					return new ResponseEntity<>(new Status(Messages.CANNOT_FIND_USER + userLogin), HttpStatus.NOT_FOUND);
    			}
    			else
    				return new ResponseEntity<>(new Status(Messages.ONLY_ADMIN_ROLE), HttpStatus.FORBIDDEN);
    		}
    		else
    			return new ResponseEntity<>(new Status(Messages.AUTH_REQ), HttpStatus.UNAUTHORIZED);
    	}
	}

    @RequestMapping(value = "/user/update", method = RequestMethod.PATCH)
    public ResponseEntity<Status> updateUser(HttpServletRequest theRequest,
    										 @RequestParam(defaultValue = "") String encodedLoginNewPassword,
    		             				 	 @RequestParam(defaultValue = "") String firstName,
    		             				 	 @RequestParam(defaultValue = "") String lastName,
    		             				     @RequestParam(defaultValue = "") String parentName)
    {

    	try(MyTransaction tr = new MyTransaction()) {
    		
    		String[] loginPassword = usersDao.getLoginPassword(theRequest);
    		if(loginPassword==null) {
    			tr.rollback();
    			return new ResponseEntity<>(new Status(Messages.AUTH_REQ), HttpStatus.BAD_REQUEST);
    		}
    	
    		String login = loginPassword[0];
    		User user = usersDao.getUserByLogin(login);
    		if(user==null) {
    			return new ResponseEntity<>(new Status(Messages.CANNOT_FIND_USER + login), HttpStatus.NOT_FOUND);
    		}

    		String newPassword = "";
    		if(encodedLoginNewPassword.length() > 0)
    			newPassword = usersDao.getLoginPassword(encodedLoginNewPassword)[1];

    		if(newPassword.length() > 0)
    			user.setPassword(newPassword);
    		if(firstName.length() > 0)
    			user.setFirstName(firstName);
    		if(lastName.length() > 0)
    			user.setLastName(lastName);
    		if(parentName.length() > 0)
    			user.setParentName(parentName);
    		
    		usersDao.update(user);
    		return new ResponseEntity<>(new Status(""), HttpStatus.ACCEPTED);
    	}
    }
    
    @RequestMapping(value = "/user/delete", method = RequestMethod.DELETE)
    public ResponseEntity<Status> deleteUser(HttpServletRequest theRequest) {

    	try(MyTransaction tr = new MyTransaction()) {
    		
    		String[] loginPassword = usersDao.getLoginPassword(theRequest);
    		if(loginPassword==null) {
    			tr.rollback();
    			return new ResponseEntity<>(new Status(Messages.AUTH_REQ), HttpStatus.BAD_REQUEST);
    		}
    	
    		String login = loginPassword[0];
    		User user = usersDao.getUserByLogin(login);
    		if(user==null) {
    			return new ResponseEntity<>(new Status(Messages.CANNOT_FIND_USER + login), HttpStatus.NOT_FOUND);
    		}
    		
    		usersDao.delete(user);
    		return new ResponseEntity<>(new Status(""), HttpStatus.ACCEPTED);
    	}
    }
}
