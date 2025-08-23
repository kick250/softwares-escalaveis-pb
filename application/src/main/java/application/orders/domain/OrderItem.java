package application.orders.domain;

public class OrderItem {
    private Long id;
    private Item item;
    private int quantity;
    private double price;

    public OrderItem(Long id, Item item, int quantity, double price) {
        this.id = id;
        this.item = item;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public Long getItemId() {
        return item.getId();
    }
}
