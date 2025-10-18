package infra.global.document.entities;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Document(collection = "carts")
public class Cart {
    @Id
    @Getter
    private String id;
    private boolean active = true;
    @Getter
    @Setter
    private Long userId = null;
    @Getter
    private Long stockId = null;
    @Getter
    @Setter
    private List<CartItem> items = new ArrayList<>();

    public void setStockId(Object stockId) {
        if (stockId instanceof Long) {
            this.stockId = (Long) stockId;
        } else if (stockId instanceof Integer) {
            this.stockId = ((Integer) stockId).longValue();
        } else {
            this.stockId = null;
        }
    }

    public Map<Long, CartItem> getItemIdPerCartItem() {
        return items.stream().collect(Collectors.toMap(CartItem::getItemId, item -> item));
    }

    public void deactivate() {
        this.active = false;
    }
}

