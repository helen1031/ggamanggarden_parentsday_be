package ggamanggarden.parentsday.orders.controller;

import ggamanggarden.parentsday.common.ResponseDTO;
import ggamanggarden.parentsday.orders.dto.OrderDTO;
import ggamanggarden.parentsday.orders.dto.OrderDetailDTO;
import ggamanggarden.parentsday.orders.entity.OrderDetailEntity;
import ggamanggarden.parentsday.orders.entity.OrderEntity;
import ggamanggarden.parentsday.orders.entity.OrderProductId;
import ggamanggarden.parentsday.orders.service.OrderDetailService;
import ggamanggarden.parentsday.orders.service.OrderService;
import ggamanggarden.parentsday.product.entity.ProductEntity;
import ggamanggarden.parentsday.product.service.ProductService;
import ggamanggarden.parentsday.sms.SMSService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SMSService smsService;

    @Transactional
    @PostMapping
    public ResponseEntity<?> create(@RequestBody OrderDTO orderDTO) {

        try {
            List<OrderDetailDTO> successfulOrderDetails = new ArrayList<>();
            StringBuilder messageTextBuilder = new StringBuilder();
            messageTextBuilder.append("까망네정원 상품을 구매해주셔서 감사합니다!\n")
                    .append("수령일은 5월 ")
                    .append(orderDTO.getWantDate())
                    .append("일이고, 수령장소는 '경상북도 김천시 대항면 대성향천길 1569-28(직지초등학교 근처)' 입니다.\n\n")
                    .append("주문 정보는 아래와 같습니다.\n");

            // Verify stock for each item and collect those that can be successfully ordered
            for (OrderDetailDTO orderDetailDTO : orderDTO.getItems()) {
                boolean stockAvailable = productService.checkAndAdjustStock(orderDetailDTO.getPid(), orderDetailDTO.getQuantity());
                if (!stockAvailable) {
                    throw new RuntimeException("Insufficient stock for product ID: " + orderDetailDTO.getPid());
                }
                successfulOrderDetails.add(orderDetailDTO);
            }

            OrderEntity orderEntity = OrderDTO.toOrderEntity(orderDTO);
            OrderEntity savedOrderEntity = orderService.create(orderEntity);

            messageTextBuilder.append("주문번호: ")
                    .append(savedOrderEntity.getOid())
                    .append("\n");

            List<OrderDetailDTO> orderDetailDTOs = orderDTO.getItems();
            for (OrderDetailDTO orderDetailDTO : orderDetailDTOs) {
                ProductEntity productEntity = productService.retrieveOne(orderDetailDTO.getPid());

                OrderDetailEntity detailEntity = OrderDetailEntity.builder()
                        .id(new OrderProductId(savedOrderEntity.getOid(), productEntity.getPid(), orderDetailDTO.getColor()))
                        .order(savedOrderEntity)
                        .product(productEntity)
                        .quantity(orderDetailDTO.getQuantity())
                        .build();

                orderDetailService.create(detailEntity);

                messageTextBuilder.append(detailEntity.getProduct().getName())
                        .append(" (")
                        .append(detailEntity.getId().getColor())
                        .append("): ")
                        .append(detailEntity.getQuantity())
                        .append("개 - ")
                        .append(detailEntity.getProduct().getPrice() * detailEntity.getQuantity())
                        .append("원\n");
            }

            OrderDTO dto = OrderDTO.builder()
                    .oid(savedOrderEntity.getOid())
                    .cname(orderEntity.getCname())
                    .phone(orderDTO.getPhone())
                    .wantDate(orderDTO.getWantDate())
                    .items(orderDetailDTOs)
                    .build();


            messageTextBuilder.append("\n※ 생화 특성 상 주문 취소 및 환불 요청은 수령일로부터 7일 이전까지 가능합니다.\n")
                    .append("※ 주문 취소 시, 결제하신 총 금액 중 \"예약 이벤트로 제공된 선물 금액(5,000원)을 제외\"하고 환불해드리니 이 점 유의 부탁드리겠습니다.");

            /**
             * 문자 발송 로직
             */
            //smsService.sendSMS(orderDTO.getPhone(), messageTextBuilder.toString());

            return ResponseEntity.ok().body(dto);

        } catch(Exception e) {
            String error = e.getMessage();
            ResponseDTO<List<OrderDTO>> response = ResponseDTO.<List<OrderDTO>>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/search")
    public ResponseEntity<List<OrderDetailEntity>> getOrderDetailsByCustomer(@RequestBody CustomerDTO customerDTO) throws Exception {
        List<OrderDetailEntity> orderDetails = orderService.findOrderDetailsByCustomerInfo(customerDTO.getCname(), customerDTO.getPhone());
        return ResponseEntity.ok(orderDetails);
    }

    @PostMapping("/mark-receive")
    public ResponseEntity<?> receiveOrder(@RequestBody OrderReceiveDTO orderReceiveDTO) {
        try {
            OrderDetailEntity updatedOrderDetail = orderService.receiveOrder(orderReceiveDTO.getOid(), orderReceiveDTO.getPid(), orderReceiveDTO.getColor());
            return ResponseEntity.ok(updatedOrderDetail);
        } catch (Exception e) {
            String error = e.getMessage();
            return ResponseEntity.badRequest().body(error);
        }
    }

}

@Getter
class CustomerDTO {
    private String cname;
    private String phone;
}

@Getter
class OrderReceiveDTO {
    private Long oid;
    private Long pid;
    private String color;
}
