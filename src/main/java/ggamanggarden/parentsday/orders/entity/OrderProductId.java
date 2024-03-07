package ggamanggarden.parentsday.orders.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductId implements Serializable {
    private Long oid; // Order ID
    private Long pid; // Product ID
    private String color;
}