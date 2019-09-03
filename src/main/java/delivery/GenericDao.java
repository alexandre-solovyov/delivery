package delivery;

import delivery.MyTransaction;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Component;

@Component
public class GenericDao {

    public void save(Object obj, MyTransaction tr) {
    	tr.session.save(obj);
    }

    public void update(Object obj, MyTransaction tr) {
    	tr.session.update(obj);
    }
    
    public void delete(Object obj, MyTransaction tr) {
        tr.session.delete(obj);
    }
    
    public Object getByField(String entity, String fieldName, String fieldValue, MyTransaction tr) {

    	Object obj = null;
        try {
        	Query query = tr.session.createQuery("From " + entity + " where " + fieldName + " = :" + fieldName);
        	query.setParameter(fieldName, fieldValue);
        	List<Object> objs = query.list();
        	if(objs.isEmpty())
        		return null;
        	else
        		return objs.get(0);
        }
        catch(Exception e) {}
        return obj;
	}
}
