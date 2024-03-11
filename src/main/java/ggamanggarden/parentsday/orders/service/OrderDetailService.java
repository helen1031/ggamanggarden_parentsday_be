package ggamanggarden.parentsday.orders.service;

import ggamanggarden.parentsday.orders.entity.OrderDetailEntity;
import ggamanggarden.parentsday.orders.repository.OrderDetailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public List<OrderDetailEntity> create(OrderDetailEntity entity) {
        if (entity == null) {
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null");
        }

        orderDetailRepository.save(entity);

        log.info("Entity ID(detail): {} is saved.", entity.getId());

        return orderDetailRepository.findAllByOid(entity.getId().getOid());
    }

    public List<OrderDetailEntity> findOrderDetailsByOid(Long oid) {
        if (oid == null) {
            log.warn("Order ID cannot be null.");
            throw new RuntimeException("Order ID cannot be null");
        }

        List<OrderDetailEntity> orderDetails = orderDetailRepository.findAllByOid(oid);
        if (orderDetails == null || orderDetails.isEmpty()) {
            log.info("No order details found for OID: {}", oid);
            return new ArrayList<>();
        }

        log.info("Retrieved {} order details for OID: {}", orderDetails.size(), oid);
        return orderDetails;
    }

    public void deleteDetails(OrderDetailEntity detail) {
        orderDetailRepository.delete(detail);
    }
}
