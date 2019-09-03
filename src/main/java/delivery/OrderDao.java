package delivery;

import delivery.Order;
import delivery.GenericDao;
import delivery.MyTransaction;

import java.util.List;

import org.hibernate.*;
import org.springframework.stereotype.Component;

@Component
public class OrderDao extends GenericDao {

	public Order cart(User user, boolean forceCreate) {

		List<Order> orders = findAllForConsumer(user);
		if(orders!=null) {
			for(Order order: orders) {
				if(order.getState()==OrderStateEnum.CART)
					return order;
			}
		}

		Order aNewOrder = new Order(user);
		save(aNewOrder);
		return aNewOrder;
	}
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<Order> findAllForConsumer(User user) {

		try {
			Query<Order> query = MyTransaction.session.createQuery("From Order where consumer_id = :consumer_id");
			query.setParameter("consumer_id", user.getId());
			return query.list();
		} catch(Exception e) {
			return null;
		}
	}
	
	public void newCart(User user) {
		cart(user, true);
	}

	public void addToCart(User user, Product product, double quantity) {
		
		Order c = cart(user, true);
		OrderItem aNew = c.add(product, quantity);
		if(aNew!=null)
			save(aNew);
	}
	
	public void confirmCart(User user) {
		Order cart = cart(user, false);
		if(cart!=null)
			cart.setState(OrderStateEnum.COMPLETED);
		update(cart);
	}
	
	public List<Order> ordersToProcess(User producer) {
		
		//TODO
    	return null;
	}
	
	public Order getByIdAndUser(int id, User user) {
		
		//TODO
		return null;
	}
}
