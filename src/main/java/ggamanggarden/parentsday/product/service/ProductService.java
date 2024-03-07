package ggamanggarden.parentsday.product.service;

import ggamanggarden.parentsday.product.entity.ProductEntity;
import ggamanggarden.parentsday.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ProductEntity create(final ProductEntity productEntity) {
        if(productEntity == null) {
            log.warn("Entity cannot be null");
            throw new RuntimeException("Entity cannot be null");
        }

        productRepository.save(productEntity);

        log.info("Entity PID : {} is saved.", productEntity.getPid());

        return productRepository.findByPid(productEntity.getPid());
    }

    public ProductEntity retrieveOne(final Long pid) {
        return productRepository.findByPid(pid);
    }

    public List<ProductEntity> retrieveAll() {return productRepository.findAll();}

    public boolean checkAndAdjustStock(Long pid, int quantity) {
        ProductEntity product = retrieveOne(pid);
        if (product.getStock() >= quantity) {
            product.setStock(product.getStock() - quantity);
            productRepository.save(product);
            return true;
        }
        return false;
    }

}
