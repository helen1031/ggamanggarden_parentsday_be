package ggamanggarden.parentsday.orders.service;

import ggamanggarden.parentsday.orders.dto.OrderDTO;
import ggamanggarden.parentsday.orders.dto.OrderDetailDTO;
import ggamanggarden.parentsday.orders.entity.OrderDetailEntity;
import ggamanggarden.parentsday.orders.entity.OrderEntity;
import ggamanggarden.parentsday.orders.repository.OrderDetailRepository;
import ggamanggarden.parentsday.orders.repository.OrderRepository;
import ggamanggarden.parentsday.product.entity.ProductEntity;
import ggamanggarden.parentsday.product.repository.ProductRepository;
import ggamanggarden.parentsday.security.EncryptionUtil;
import ggamanggarden.parentsday.sms.SMSService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    // 1. 주문 생성
    public OrderEntity create(final OrderEntity orderEntity) throws Exception {
        if (orderEntity == null) {
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null");
        }

        orderEntity.setCname(EncryptionUtil.encrypt(orderEntity.getCname()));
        orderEntity.setPhone(EncryptionUtil.encrypt(orderEntity.getPhone()));

        orderRepository.save(orderEntity);

        log.info("Entity OID : {} is saved.", orderEntity.getOid());

        return orderRepository.findByOid(orderEntity.getOid());
    }

    public OrderEntity findOrderByOid(final Long oid) {
        return orderRepository.findByOid(oid);
    }

    // 2. 주문 내역 조회
    public List<OrderDetailEntity> findOrderDetailsByCustomerInfo(String cname, String phone) throws Exception {
        List<OrderEntity> orders = orderRepository.findAllByCnameAndPhone(EncryptionUtil.encrypt(cname),
                EncryptionUtil.encrypt(phone));
        List<OrderDetailEntity> orderDetails = new ArrayList<>();
        for (OrderEntity order : orders) {
            List<OrderDetailEntity> details = orderDetailRepository.findAllByOid(order.getOid());
            orderDetails.addAll(details);
        }
        return orderDetails;
    }

    public List<OrderDetailEntity> findOrderDetailsByOid(Long oid) {
        return orderDetailRepository.findAllByOid(oid);
    }

    // 3. 수령 여부 체크
    public OrderDetailEntity receiveOrder(Long oid, Long pid, String color) {
        // Fetch the order and order detail based on oid, pid, and color
        OrderDetailEntity orderDetail = orderDetailRepository.findByOidAndPidAndColor(oid, pid, color)
                .orElseThrow(() -> new RuntimeException("Order detail not found"));

        // Update the pickUp state and actualDate
        orderDetail.setPickUp(true);
        orderDetail.getOrder().setActualDate(new Date()); // Use java.util.Date or java.time.LocalDateTime based on your entity definition

        // Save the updated order detail
        orderDetailRepository.save(orderDetail);

        return orderDetail;
    }

    // 4. 주문 내역 삭제
    public void deleteOrder(Long oid) {
        OrderEntity existingOrder = orderRepository.findByOid(oid);
        if(existingOrder == null) {
            throw new RuntimeException("Order not found");
        }

        orderRepository.delete(existingOrder);
        log.info("Entity OID : {} is deleted.", oid);
    }

}
