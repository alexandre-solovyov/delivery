package delivery;

import java.lang.AutoCloseable;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class MyTransaction implements AutoCloseable {

	public static Session session;
	private Transaction transaction;
	private boolean active;
	
	public MyTransaction() {
		if(session==null)
			session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
				
		transaction = session.beginTransaction();
		//System.out.println("begin transaction");
		active = true;
	}

	public void commit() {
		//System.out.println("external commit");
		transaction.commit();
		active = false;
	}

	public void rollback() {
		//System.out.println("external rollback");
		transaction.rollback();
		active = false;
	}
	
	@Override
	public void close()  {
		if(active) {
			transaction.commit();
			//System.out.println("commit transaction");
			active = false;
		}
	}
}
