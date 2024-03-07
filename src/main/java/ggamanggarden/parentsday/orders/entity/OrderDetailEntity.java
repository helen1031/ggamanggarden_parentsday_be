package ggamanggarden.parentsday.orders.entity;

import ggamanggarden.parentsday.product.entity.ProductEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "order_details")
public class OrderDetailEntity {

    @EmbeddedId
    private OrderProductId id;

    @MapsId("oid")
    @ManyToOne
    @JoinColumn(name = "oid", referencedColumnName = "oid")
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "pid", referencedColumnName = "pid", insertable = false, updatable = false)
    private ProductEntity product; // Relationship to ProductEntity

    // Other details specific to the order-product relationship
    private Integer quantity;
    private boolean pickUp;
}