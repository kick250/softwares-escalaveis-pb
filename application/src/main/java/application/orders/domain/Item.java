package application.orders.domain;

public class Item {
    private Long id;
    private Long stockId;
    private double price;

    public Item(Long id, Long stockId, double price) {
        this.id = id;
        this.stockId = stockId;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public boolean isFromStock(Long stockId) {
        return this.stockId.equals(stockId);
    }

    public double getPrice() {
        return price;
    }
}
