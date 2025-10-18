package infra.global.document.entities;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cart_items")
public class CartItem {
    @Id
    private String id;
    @Getter
    @Setter
    private Long itemId;
    @Getter
    @Setter
    private int quantity;
}