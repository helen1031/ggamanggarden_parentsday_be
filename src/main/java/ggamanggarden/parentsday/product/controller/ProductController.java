package ggamanggarden.parentsday.product.controller;

import ggamanggarden.parentsday.common.ResponseDTO;
import ggamanggarden.parentsday.product.dto.ProductDTO;
import ggamanggarden.parentsday.product.entity.ProductEntity;
import ggamanggarden.parentsday.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<?> retrieveProducts() {
        List<ProductEntity> productEntities = productService.retrieveAll();

        List<ProductDTO> productDTOs = new ArrayList<>();

        for (ProductEntity productEntity: productEntities) {
            ProductDTO productDTO = ProductDTO.builder()
                    .pid(productEntity.getPid())
                    .name(productEntity.getName())
                    .price(productEntity.getPrice())
                    .stock(productEntity.getStock())
                    .colorOption(productEntity.isColorOption())
            .build();

            productDTOs.add(productDTO);
        }
        return ResponseEntity.ok().body(productDTOs);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductDTO dto) {
        try {
            ProductEntity entity = ProductDTO.toEntity(dto);
            entity.setPid(null);

            ProductEntity savedEntity = productService.create(entity);

            ProductDTO savedDto = ProductDTO.fromEntity(savedEntity);

            List<ProductDTO> dtos = Collections.singletonList(savedDto);

            ResponseDTO<ProductDTO> response = ResponseDTO.<ProductDTO>builder().data(dtos).build();

            return ResponseEntity.ok(response);
        } catch(Exception e) {
            String error = e.getMessage();
            ResponseDTO<ProductDTO> response = ResponseDTO.<ProductDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}
