package com.example.shoppingmall.order.servie;

import com.example.shoppingmall.com.exception.SoldOutException;
import com.example.shoppingmall.com.utils.JsonReaderUtils;
import com.example.shoppingmall.order.dto.OrderProductDto;
import com.example.shoppingmall.product.entity.Product;
import com.example.shoppingmall.product.service.ProductService;
import com.example.shoppingmall.resources.order.TestConstants;
import com.example.shoppingmall.stock.service.StockService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    private JsonReaderUtils jsonReaderUtils;

    @BeforeEach
    void init() {
        jsonReaderUtils = new JsonReaderUtils(TestConstants.TEST_FILE_PATH + "/order");
    }

    private List<OrderProductDto> makeProductDtoList(OrderProductDto... orderProductDto) {
        return new ArrayList<>(Arrays.asList(orderProductDto));
    }

    @Test
    @DisplayName("성공 테스트")
    void success_test() {
        OrderProductDto sample = jsonReaderUtils.getObject("/OrderSample.json", OrderProductDto.class);
        Product beforeOrderProduct = productService.getProductById(sample.getProductId());

        orderService.order(makeProductDtoList(sample));

        Product orderedProduct = productService.getProductById(sample.getProductId());
        Assertions.assertThat(orderedProduct.getQuantity()).isEqualTo(beforeOrderProduct.getQuantity() - sample.getOrderQuantity());
    }

    @Test
    @DisplayName("재고보다 많은 수량을 주문할 경우 StockOutException 발생한다.")
    void out_of_stock() {
        OrderProductDto sample = jsonReaderUtils.getObject("/OrderSample.json", OrderProductDto.class);
        Product productById = productService.getProductById(sample.getProductId());
        int moreQuantity = productById.getQuantity() + 1;
        OrderProductDto orderProductDto = OrderProductDto.builder().productId(sample.getProductId()).orderQuantity(moreQuantity).build();
        List<OrderProductDto> orderProductDtoList = makeProductDtoList(orderProductDto);

        Assertions.assertThatThrownBy(() -> orderService.order(orderProductDtoList))
                .isInstanceOf(SoldOutException.class);
    }


    @Test
    @DisplayName("재고 수 만큼의 다중 쓰레드가 각각 1개씩 상품을 주문했을 경우, 재고는 0개이다.")
    void order_by_stock_multi_thread() throws InterruptedException {
        OrderProductDto sample = jsonReaderUtils.getObject("/OrderSample.json", OrderProductDto.class);
        List<OrderProductDto> orderProductDtoList = new ArrayList<>();
        orderProductDtoList.add(sample);

        Product product = productService.getProductById(sample.getProductId());
        int threadCount = product.getQuantity();

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    orderService.order(orderProductDtoList);
                }catch (SoldOutException e){
                    throw e;
                } catch (Exception e){
                    throw new RuntimeException(e);
                }finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Product updatedProduct = productService.getProductById(sample.getProductId());
        Assertions.assertThat(updatedProduct.getQuantity()).isEqualTo(0);
    }

    @Test
    @DisplayName("동시성 제어가 안되어있는 경우, 재고 수 만큼의 다중 쓰레드가 각각 1개씩 상품을 주문했을 경우, 재고는 0개가 아니아.")
    void order_by_stock_mult_thread_not_lock() throws InterruptedException {
        StockService stockService = new StockService(productService);
        OrderService orderService = new OrderService(stockService);

        OrderProductDto sample = jsonReaderUtils.getObject("/OrderSample.json", OrderProductDto.class);
        List<OrderProductDto> orderProductDtoList = new ArrayList<>();
        orderProductDtoList.add(sample);

        Product product = productService.getProductById(sample.getProductId());
        int threadCount = product.getQuantity();

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    orderService.order(orderProductDtoList);
                }catch (SoldOutException e){
                    throw e;
                } catch (Exception e){
                    throw new RuntimeException(e);
                }finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Product updatedProduct = productService.getProductById(sample.getProductId());
        Assertions.assertThat(updatedProduct.getQuantity()).isNotEqualTo(0);
    }

}