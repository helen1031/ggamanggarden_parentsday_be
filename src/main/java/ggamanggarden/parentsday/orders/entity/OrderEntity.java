package ggamanggarden.parentsday.orders.entity;

import ggamanggarden.parentsday.product.entity.ProductEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long oid; // Auto-generated primary key for the order itself

    // Order-specific fields
    private String cname;
    private String phone;
    private String wantDate;
    @CreationTimestamp
    private Date orderDate;
    private Date actualDate;
}