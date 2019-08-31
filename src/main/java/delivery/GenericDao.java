package delivery;

import delivery.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.*;
import org.springframework.stereotype.Component;

@Component
public class GenericDao {

    public void save(Object obj) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tr = session.beginTransaction();
        session.save(obj);
        tr.commit();
        session.close();
    }

    public void delete(Object obj) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tr = session.beginTransaction();
        session.delete(obj);
        tr.commit();
        session.close();
    }
}
