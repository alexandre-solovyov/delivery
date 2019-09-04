package delivery;

import java.util.LinkedList;
import java.util.List;

class OrderItemJson
{
	private String product;
    private double quantity;
    private double price;

    public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
    
	public OrderItemJson(OrderItem item) {
		
		this.product = item.getProduct().getName();
		this.quantity = item.getQuantity();
		this.price = item.getFinalPrice() > 0 ? item.getFinalPrice() : item.getProduct().getPrice();
	}
}

public class OrderJson
{
	private List<OrderItemJson> items;
	private double price;

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
		
		this.items = new LinkedList<OrderItemJson>();
		if(order.getItems()!=null)
			for(OrderItem item: order.getItems())
				this.items.add(new OrderItemJson(item));
		this.price = order.getTotalPrice();
	}
}
