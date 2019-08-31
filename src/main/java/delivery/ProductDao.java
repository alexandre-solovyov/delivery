package delivery;

import delivery.Product;
import delivery.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ProductDao {

    public void save(Product product) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tr = session.beginTransaction();
        session.save(product);
        tr.commit();
        session.close();
    }

    public void delete(Product product) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tr = session.beginTransaction();
        session.delete(product);
        tr.commit();
        session.close();
    }
}
