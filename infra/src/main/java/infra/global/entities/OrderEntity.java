package infra.global.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity(name="Order")
@Table(name="orders")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;
    private boolean deleted = false;
    @Getter
    @Setter
    private Instant createdAt;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserEntity owner;
}
