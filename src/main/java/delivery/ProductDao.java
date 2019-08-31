package delivery;

import delivery.Product;
import org.springframework.stereotype.Component;
import org.hibernate.Session;
import java.util.*;
import delivery.GenericDao;

@Component
public class ProductDao extends GenericDao {

    public List<Product> findAll() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<Product> products = session.createQuery("From Product").list();
        return products;
    }
}
