package delivery;

import delivery.User;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import delivery.GenericDao;

@Component
public class UserDao extends GenericDao {

	private final String TOKEN = "token";
	
    public String[] getLoginPassword(HttpServletRequest theRequest) {
    	
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
       
	public boolean checkPassword(String login, String password, MyTransaction tr) {
		
		User user = getUserByLogin(login, tr);
		if(user==null)
			return false;

		String enc_pass = User.encode(password);
		return user.encryptedPassword().contentEquals(enc_pass);
	}

	public void generateToken(HttpServletResponse theResponse, String login, MyTransaction tr) {
		
		User user = getUserByLogin(login, tr);
		String token = user.login() + ":" + "OK";
	    Cookie cookie = new Cookie(TOKEN, token);
	    theResponse.addCookie(cookie);		
	}

	public User currentUser(HttpServletRequest theRequest, MyTransaction tr) {
    	
    	Cookie[] cookies = theRequest.getCookies();
    	if(cookies==null)
    		return null;
    		
    	for(Cookie cookie: cookies)
    		if(cookie.getName().contentEquals(TOKEN))
    		{
    			String token = cookie.getValue();
    			String[] parts = token.split(":");
    			if(parts.length==2)
    			{
    				User user = getUserByLogin(parts[0], tr);
    				return user;
    			}
    		}
    
    	return null;
	}
	
	public User getUserByLogin(String login, MyTransaction tr) {

		return (User)getByField("User", "login", login, tr);
	}
	
	public boolean changeRole(String login, UserRoleEnum newRole, MyTransaction tr) {

		User user = getUserByLogin(login, tr);
		if(user!=null)
		{
			user.setRole(newRole);
			tr.session.update(user);
		}
		return true;
	}
	
    public List<User> findAll(MyTransaction tr) {

        List<User> users = tr.session.createQuery("From User").list();
        return users;
    }
}
