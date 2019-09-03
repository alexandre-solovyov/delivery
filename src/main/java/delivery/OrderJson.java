package delivery;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;

import delivery.Order;
import delivery.OrderItem;

class OrderItemJson
{
    public String product;
    public double quantity;
    public double price;
	
	public OrderItemJson(OrderItem item) {
		
	}
}

public class OrderJson
{
	public List<OrderItemJson> items;
	public double price;
	
	public List<OrderItemJson> getItems() {
		return items;
	}

	public void setItems(List<OrderItemJson> items) {
		this.items = items;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	public OrderJson(Order order) {
		
		items = new LinkedList<OrderItemJson>();
		if(order.getItems()!=null)
			for(OrderItem item: order.getItems())
				items.add(new OrderItemJson(item));
		price = order.getTotalPrice();
	}
}
