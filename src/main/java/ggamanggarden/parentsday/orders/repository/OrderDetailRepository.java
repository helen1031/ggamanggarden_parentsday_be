package ggamanggarden.parentsday.orders.repository;

import ggamanggarden.parentsday.orders.entity.OrderDetailEntity;
import ggamanggarden.parentsday.orders.entity.OrderProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, OrderProductId> {

    @Query("SELECT od FROM OrderDetailEntity od WHERE od.order.oid = :oid")
    List<OrderDetailEntity> findAllByOid(@Param("oid")Long Oid);

    @Query("SELECT od FROM OrderDetailEntity od WHERE od.id.oid = :oid AND od.id.pid = :pid AND od.id.color = :color")
    Optional<OrderDetailEntity> findByOidAndPidAndColor(Long oid, Long pid, String color);


}
