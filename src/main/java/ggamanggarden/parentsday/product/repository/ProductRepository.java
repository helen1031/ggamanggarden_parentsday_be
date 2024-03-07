package ggamanggarden.parentsday.product.repository;

import ggamanggarden.parentsday.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Override
    List<ProductEntity> findAll();

    ProductEntity findByPid(Long pid);
}
