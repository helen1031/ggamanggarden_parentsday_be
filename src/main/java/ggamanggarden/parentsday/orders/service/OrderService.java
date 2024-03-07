package ggamanggarden.parentsday.orders.service;

import ggamanggarden.parentsday.orders.entity.OrderDetailEntity;
import ggamanggarden.parentsday.orders.entity.OrderEntity;
import ggamanggarden.parentsday.orders.repository.OrderDetailRepository;
import ggamanggarden.parentsday.orders.repository.OrderRepository;
import ggamanggarden.parentsday.product.repository.ProductRepository;
import ggamanggarden.parentsday.security.EncryptionUtil;
import ggamanggarden.parentsday.sms.SMSService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

}
