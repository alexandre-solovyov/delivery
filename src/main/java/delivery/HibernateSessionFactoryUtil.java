package delivery;

import delivery.Product;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import org.hibernate.boot.registry.*;
import org.hibernate.boot.*;

public class HibernateSessionFactoryUtil {
    private static SessionFactory sessionFactory;

    private HibernateSessionFactoryUtil() {}

    /*public static SessionFactory getSessionFactory() {
        System.out.println("1");
        if (sessionFactory == null) {
            try {
                System.out.println("2");
                Configuration configuration = new Configuration().configure();
                System.out.println("3");
                configuration.addAnnotatedClass(Product.class);
                System.out.println("4");
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                System.out.println("5");
                sessionFactory = configuration.buildSessionFactory(builder.build());
                System.out.println("6");

            } catch (Exception e) {
                System.out.println("Exception: " + e);
            }
        }
        return sessionFactory;
    }*/

    public static SessionFactory getSessionFactory(){

        StandardServiceRegistry registry;
        SessionFactory sessionFactory;
        registry = new StandardServiceRegistryBuilder().configure().build();
    
        MetadataSources sources = new MetadataSources(registry);
    
        Metadata metadata = sources.getMetadataBuilder().build();
    
        sessionFactory = metadata.getSessionFactoryBuilder().build();
        return sessionFactory;
    
    }
}
