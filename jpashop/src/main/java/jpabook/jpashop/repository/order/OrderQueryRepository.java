package jpabook.jpashop.repository.order;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.dto.OrderFlatDto;
import jpabook.jpashop.dto.OrderItemQueryDto;
import jpabook.jpashop.dto.OrderQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    public List<OrderQueryDto> findOrderQueryDtos(){
        List<OrderQueryDto> orders = findOrders();

        for (OrderQueryDto order : orders) {
            List<OrderItemQueryDto> orderItems = findOrderItems(order.getOrderId());
            order.setOrderItems(orderItems);
        }

        return orders;
    }

    public List<OrderQueryDto> findOrderQueryDtos_optimization() {
        List<OrderQueryDto> orders = findOrders();

        List<Long> orderIds = orders.stream()
                .map(OrderQueryDto::getOrderId)
                .toList();

        List<OrderItemQueryDto> orderItems = em.createQuery(
                        " select new jpabook.jpashop.dto.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                                "from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        Map<Long, List<OrderItemQueryDto>> itemMapByOrderId = orderItems.stream()
                .collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));

        for (OrderQueryDto order : orders) {
            if(itemMapByOrderId.containsKey(order.getOrderId())){
                order.setOrderItems(itemMapByOrderId.get(order.getOrderId()));
            }
        }

        return orders;
    }

    private List<OrderQueryDto> findOrders(){
        return em.createQuery(
                "select new jpabook.jpashop.dto.OrderQueryDto(o.id, m.name, o.orderDate, o.orderStatus, d.address)" +
                        "from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderQueryDto.class
        ).getResultList();
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId){
        return em.createQuery(
                " select new jpabook.jpashop.dto.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                        "from OrderItem oi" +
                        " join oi.item i" +
                        " where oi.order.id = :orderId", OrderItemQueryDto.class
                )
                .setParameter("orderId", orderId)
                .getResultList();
    }

    public List<OrderFlatDto> findOrderQueryDtos_flat() {
        return em.createQuery(
                        "select new jpabook.jpashop.dto.OrderFlatDto(o.id, m.name, o.orderDate, o.orderStatus, d.address, i.name, oi.orderPrice, oi.count)" +
                                " from Order o" +
                                " join o.member m" +
                                " join o.delivery d" +
                                " join o.orderItems oi" +
                                " join oi.item i", OrderFlatDto.class)
                .getResultList();
    }

}
