package delivery;

import javax.persistence.*;

@Entity
@Table (name = "orders_details")
class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="order_id", nullable=false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "final_price")
    private double finalPrice;

    @Column(name = "quantity")
    private double quantity;

    public OrderItem() {}
    public OrderItem(Order order, Product product, double quantity)
    {
    	this.order = order;
        this.product = product;
        this.quantity = quantity;
    }

    public int getId() { return id; }
    public Product getProduct() { return product; }
    public double getFinalPrice() { return finalPrice; }
    
    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public double getTotalPrice()
    {
        if(finalPrice > 0)
            return finalPrice * quantity;
        else
            return product.getPrice() * quantity;
    }
}
