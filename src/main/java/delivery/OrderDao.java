package delivery;

import delivery.Order;
import delivery.GenericDao;
import delivery.MyTransaction;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Component;

@Component
public class OrderDao extends GenericDao {

	public Order cart(User user, boolean forceCreate, MyTransaction tr) {

		List<Order> orders = findAllForConsumer(user, tr);
		if(orders!=null) {
			for(Order order: orders) {
				if(order.getState()==OrderStateEnum.CART)
					return order;
			}
		}

		Order aNewOrder = new Order(user);
		save(aNewOrder, tr);
		return aNewOrder;
	}
	
	public List<Order> findAllForConsumer(User user, MyTransaction tr) {

		try {
			Query query = tr.session.createQuery("From Order where consumer_id = :consumer_id");
			query.setParameter("consumer_id", user.getId());
			return query.list();
		} catch(Exception e) {
			return null;
		}
	}
	
	public void newCart(User user, MyTransaction tr) {
		cart(user, true, tr);
	}

	public void addToCart(User user, Product product, double quantity, MyTransaction tr) {
		
		Order c = cart(user, true, tr);
		OrderItem aNew = c.add(product, quantity);
		if(aNew!=null)
			save(aNew, tr);
	}
	
	public void confirmCart(User user, MyTransaction tr) {
		Order cart = cart(user, false, tr);
		if(cart!=null)
			cart.setState(OrderStateEnum.COMPLETED);
		update(cart, tr);
	}
	
	public List<Order> ordersToProcess(User producer, MyTransaction tr) {
		
		//TODO
    	return null;
	}
	
	public Order getByIdAndUser(int id, User user, MyTransaction tr) {
		
		//TODO
		return null;
	}
}
