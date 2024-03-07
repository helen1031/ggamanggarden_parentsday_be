package ggamanggarden.parentsday.product.dto;

import ggamanggarden.parentsday.product.entity.ProductEntity;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long pid;
    private String name;
    private Integer price;
    private Integer stock; // 재고

    private boolean colorOption; // 색상 선택 가능 여부

    public ProductDTO(final ProductEntity entity) {
        this.pid = entity.getPid();
        this.name = entity.getName();
        this.price = entity.getPrice();
        this.stock = entity.getStock();
        this.colorOption = entity.isColorOption();
    }

    public static ProductDTO fromEntity(ProductEntity entity) {
        ProductDTO dto = new ProductDTO();
        dto.setPid(entity.getPid());
        dto.setName(entity.getName());
        dto.setPrice(entity.getPrice());
        dto.setStock(entity.getStock());
        dto.setColorOption(entity.isColorOption());
        return dto;
    }

    public static ProductEntity toEntity(final ProductDTO dto) {
        return ProductEntity.builder()
                .pid(dto.getPid())
                .name(dto.getName())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .colorOption(dto.isColorOption())
                .build();
    }


}
