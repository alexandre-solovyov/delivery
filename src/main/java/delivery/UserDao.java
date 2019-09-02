package delivery;

import delivery.User;
import org.springframework.stereotype.Component;
import org.hibernate.Session;

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
       
	public boolean checkPassword(String login, String password) {
		//TODO
		return true;
	}

	public void generateToken(HttpServletResponse theResponse, String login) {
		String token = login + "OK";
	    Cookie cookie = new Cookie(TOKEN, token);
	    theResponse.addCookie(cookie);		
	}
	
    public UserRoleEnum currentRole(HttpServletRequest theRequest) {
    	
    	Cookie[] cookies = theRequest.getCookies();
    	if(cookies==null)
    		return UserRoleEnum.NOT_SIGNED_IN;
    		
    	for(Cookie cookie: cookies)
    		if(cookie.getName().contentEquals(TOKEN))
    		{
    			String token = cookie.getValue();
    			//String[] parts = token.split(regex)
    		}
    
    	return UserRoleEnum.USER;
	}


	public boolean hasUser(String login) {
		//TODO
		return false;
	}

    public List<User> findAll() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<User> users = session.createQuery("From User").list();
        return users;
    }
}
