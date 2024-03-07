package ggamanggarden.parentsday.orders.dto;

import lombok.*;

import java.util.Date;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
    private Long pid;
    private Integer quantity;
    private String color;
}