package com.example.hello.order;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@Slf4j
@SpringBootTest
class OrderServiceTest {

    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    void complete() throws NotEnoughMoneyException {
        //given
        Order order = new Order();
        order.setUsername("정상");
        //when
        orderService.order(order);
        //then
        Order foundOrder = orderRepository.findById(order.getId()).get();
        Assertions.assertThat(foundOrder.getPayStatus()).isEqualTo("완료");
    }

    @Test
    void runtimeException() {
        //unchecked 예외 -> 롤백

        //given
        Order order = new Order();
        order.setUsername("예외");
        //when
        Assertions.assertThatThrownBy(() -> orderService.order(order)).isInstanceOf(RuntimeException.class);
        //then
        Optional<Order> foundOrder = orderRepository.findById(order.getId());
        Assertions.assertThat(foundOrder.isEmpty()).isTrue();
    }

    @Test
    void bizException(){
        //checked 예외가 발생 -> db에 커밋을 한다.

        //given
        Order order = new Order();
        order.setUsername("잔고 부족");
        //when
        Assertions.assertThatThrownBy(() -> orderService.order(order)).isInstanceOf(NotEnoughMoneyException.class);
        //then
        Order foundOrder = orderRepository.findById(order.getId()).get();
        Assertions.assertThat(foundOrder.getPayStatus()).isEqualTo("대기");
    }
}