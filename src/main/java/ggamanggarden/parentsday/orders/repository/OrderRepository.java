package ggamanggarden.parentsday.orders.repository;

import ggamanggarden.parentsday.orders.entity.OrderDetailEntity;
import ggamanggarden.parentsday.orders.entity.OrderEntity;
import ggamanggarden.parentsday.orders.entity.OrderProductId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, OrderProductId> {
    @Override
    List<OrderEntity> findAll();

    List<OrderEntity> findAllByCnameAndPhone(String cname, String phone);

    OrderEntity findByOid(Long oid);
}
