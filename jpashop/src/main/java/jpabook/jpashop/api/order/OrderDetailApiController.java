package jpabook.jpashop.api.order;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.dto.OrderDto;
import jpabook.jpashop.dto.OrderFlatDto;
import jpabook.jpashop.dto.OrderQueryDto;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.order.OrderQueryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class OrderDetailApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllWithItem();

        return orders.stream()
                .map(OrderDto::new)
                .collect(toList());
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3(){
        List<Order> orders = orderRepository.findAllWithMemberDelivery();

        return orders.stream()
                .map(OrderDto::new)
                .collect(toList());
    }

    // 주문정보쿼리 패치 조인
    // batch size로 주문상품정보 쿼리 활용 ()
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                        @RequestParam(value = "limit", defaultValue = "100") int limit) {

        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);

        return orders.stream()
                .map(OrderDto::new)
                .collect(toList());
    }

    // 주문정보 쿼리, for문 돌면서 주문번호 당 주문상품정보 쿼리 조회
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4(){
        return orderQueryRepository.findOrderQueryDtos();
    }

    // 주문정보 쿼리, 주문상품정보 쿼리
    // 주문정보 쿼리 페이징 처리 그 후 in 절로 주문상품정보 쿼리 조회
    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5(){
        return orderQueryRepository.findOrderQueryDtos_optimization();
    }

    // flat하게 조회
    // 한방쿼리로 가져올 수 있지만 주문번호 기준으로 페이징처리는 불가
    @GetMapping("/api/v6/orders")
    public List<OrderFlatDto> ordersV6(){
        return orderQueryRepository.findOrderQueryDtos_flat();
    }

}
