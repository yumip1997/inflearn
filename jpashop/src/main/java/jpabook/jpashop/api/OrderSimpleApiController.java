package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import jpabook.jpashop.dto.OrderSimpleQueryDto;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){
        return orderRepository.findAllByString(new OrderSearch());
    }

    /**
     * V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용X)
     * - 단점: 지연로딩으로 쿼리 N번 호출
     */
    @GetMapping("/api/v2/simple-orders")
    public List<OrderSimpleQueryDto> ordersV2() {
        // N+1 문제 발생
        // SimpleOrderQueryDto 접근시 orders 개수만큼 member, address 조회
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        return orders.stream()
                .map(OrderSimpleQueryDto::new)
                .collect(toList());
    }

    @GetMapping("/api/v3/simple-orders")
    public List<OrderSimpleQueryDto> ordersV3() {
        List<Order> allWithMemberDelivery = orderRepository.findAllWithMemberDelivery();

        // 쿼리할때 Dto 외의 컬럼도 조회
        // v4 보다 repository 재사용성이 높은
        return allWithMemberDelivery.stream()
                .map(OrderSimpleQueryDto::new)
                .toList();
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        // 원하는 컬럼만 매핑해서 가져올 수 있음
        // v3 보다는 repository 재사용성이 떨어짐 -> OrderSimpleQueryDto 만을 위한 쿼리
        return orderRepository.findOrderDtos();
    }

}
