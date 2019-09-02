package delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import delivery.UserDao;
import delivery.Status;

@RestController
public class UsersController {

	private final String TOKEN = "token";
	
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
    
    

    private String[] getLoginPassword(HttpServletRequest theRequest) {
    	
    	String auth = theRequest.getHeader("Authorization");
    	//System.out.println("Authorization: " + auth);
    	
    	if (auth == null || !auth.toLowerCase().startsWith("basic"))
    		return null;
    		
  	    // Authorization: Basic base64credentials
   	    String base64Credentials = auth.substring("Basic".length()).trim();
   	    byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
   	    String credentials = new String(credDecoded, StandardCharsets.UTF_8);
   	    
   	    // credentials = username:password
   	    String[] values = credentials.split(":", 2);
   	    //System.out.println("Login: " + values[0]);
   	    //System.out.println("Password: " + values[1]);
   	    
   	    return values;
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Status login(HttpServletRequest theRequest,
    		            HttpServletResponse theResponse) {

    	String[] loginPassword = getLoginPassword(theRequest);
    	if(loginPassword==null)
    		return new Status("Basic authorization is required");
    				
   	    if(!usersDao.checkPassword(loginPassword[0], loginPassword[1]))
   	    	return new Status("Invalid login/password");
    	
	    String token = usersDao.generateToken(loginPassword[0]);
	    Cookie cookie = new Cookie(TOKEN, token);
	    theResponse.addCookie(cookie);
	    return new Status("");
    }
    
    @RequestMapping(value = "/op", method = RequestMethod.POST)
    public Status operation(HttpServletRequest theRequest) {
    	
    	Cookie[] cookies = theRequest.getCookies();
    	if(cookies!=null) {
    		for(Cookie cookie: cookies)
    			if(cookie.getName().contentEquals(TOKEN))
    			{
    				String token = cookie.getValue();
    				if(token.contentEquals("alexok"))
    					return new Status("");
    			}
    	}
    	return new Status("Please log in");
    }
    
}
