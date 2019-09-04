package delivery;

import java.util.List;

import org.hibernate.*;
import org.springframework.stereotype.Component;

@Component
public class GenericDao {

    public void save(Object obj) {
    	MyTransaction.session.save(obj);
    }

    public void update(Object obj) {
    	MyTransaction.session.update(obj);
    }
    
    public void delete(Object obj) {
    	MyTransaction.session.delete(obj);
    }
    
    @SuppressWarnings({ "deprecation", "unchecked" })
    public Object getByField(String entity, String fieldName, String fieldValue) {

    	Object obj = null;
        try {
        	String request = String.format("From %s where %s = :%s", entity, fieldName, fieldName); 
        	Query<Object> query = MyTransaction.session.createQuery(request);
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
