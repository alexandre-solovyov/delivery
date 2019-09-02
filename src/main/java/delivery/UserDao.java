package delivery;

import delivery.User;
import org.springframework.stereotype.Component;
import org.hibernate.Session;
import java.util.*;
import delivery.GenericDao;

@Component
public class UserDao extends GenericDao {

	public boolean checkPassword(String login, String password) {
		//TODO
		return true;
	}

	public String generateToken(String login) {
		//TODO
		return login + "OK";
	}
	
    public List<User> findAll() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<User> users = session.createQuery("From User").list();
        return users;
    }
}
