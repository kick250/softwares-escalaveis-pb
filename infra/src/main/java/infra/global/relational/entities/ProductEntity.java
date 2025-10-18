package infra.global.relational.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity(name="Product")
@Table(name="products")
@AllArgsConstructor
@NoArgsConstructor
public class ProductEntity extends Attachable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Getter
    private Long id;
    private boolean deleted = false;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String description;
    @OneToMany
    @JoinColumn(name = "product_id")
    private List<StockItemEntity> stockItems;


    public ProductEntity(String name, String description, AttachmentEntity attachmentEntity) {
        this.name = name;
        this.description = description;
        this.attachmentEntity = attachmentEntity;
    }

    public void delete() {
        this.deleted = true;
        if (attachmentEntity != null) {
            attachmentEntity.delete();
        }
        stockItems.forEach(StockItemEntity::delete);
    }

    public boolean hasAttachment() {
        return attachmentEntity != null;
    }

    public String getImageBase64() {
        if (attachmentEntity != null)
            return attachmentEntity.getFileBase64();

        return null;
    }

    public String getImageType() {
        if (attachmentEntity != null)
            return attachmentEntity.getFileType();

        return null;
    }
}
