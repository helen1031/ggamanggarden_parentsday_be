package ggamanggarden.parentsday.orders.dto;

import ggamanggarden.parentsday.orders.entity.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Long oid; // This will be null when creating a new order and populated for existing orders
    private String cname;
    private String phone;
    private boolean pickUp;
    private String wantDate;

    private Date actualDate;
    private List<OrderDetailDTO> items;


    public OrderDTO(final OrderEntity entity) {
        this.cname = entity.getCname();
        this.phone = entity.getPhone();
        this.wantDate= entity.getWantDate();
        this.actualDate=entity.getActualDate();
    }

    public static OrderEntity toOrderEntity(OrderDTO dto) {
        // Convert OrderDTO to OrderEntity (excluding items)
        return OrderEntity.builder()
                .cname(dto.getCname())
                .phone(dto.getPhone())
                .wantDate(dto.getWantDate())
                .actualDate(dto.getActualDate())
                .build();
    }
}
