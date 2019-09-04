package delivery;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table (name = "orders")
class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id")
    private List<OrderItem> items;

    @Column(name = "state")
    private OrderStateEnum state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consumer_id")
    private User consumer;
    
    public Order() {}
    public Order(User consumer) {
    	this.consumer = consumer;
    	this.state = OrderStateEnum.CART;
    	this.items = new ArrayList<OrderItem>();
    }

    public int getId() { return id; }

    public List<OrderItem> getItems() { return items; }
    public void clear() { items.clear(); }
    public OrderItem add(Product product, double quantity) {
    	
    	int code = product.getCode();
    	
    	if(items!=null) {
    		for(OrderItem item: items) {
    			if(item.getProduct().getCode()==code) {
    				item.setQuantity(item.getQuantity() + quantity);
    				return null;
    			}
    		}
    	}

    	OrderItem aNewItem = new OrderItem(this, product, quantity);
    	items.add(aNewItem);
    	return aNewItem;
    }
    public void remove(Product product) {

    	LinkedList<OrderItem> to_remove = new LinkedList<OrderItem>();
    	int code = product.getCode();
    	for(OrderItem item: items) {
    		if(item.getProduct().getCode()==code) {
    			to_remove.add(item);
    		}
    	}
    	for(OrderItem item: to_remove)
    		items.remove(item);
    }

    public OrderStateEnum getState() { return state; }
    public void setState(OrderStateEnum state) { this.state = state; }

    public double getTotalPrice() {
        double price = 0;
        for(OrderItem item: items)
            price += item.getTotalPrice();
        return price;
    }
}
