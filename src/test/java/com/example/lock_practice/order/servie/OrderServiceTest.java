package com.example.lock_practice.order.servie;

import com.example.lock_practice.product.dto.ProductDto;
import com.example.lock_practice.product.entity.Product;
import com.example.lock_practice.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
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

    private ProductDto getSample(){
        return new ProductDto("768848", 1);
    }

    private List<List<ProductDto>> makeSampleData(ProductDto productDto, int cnt){
        List<List<ProductDto>> productDtoList = new ArrayList<>();
        for(int i=0;i<cnt;i++){
            List<ProductDto> orderReq = new ArrayList<>();
            orderReq.add(new ProductDto(productDto.getProductId(), productDto.getQuantity()));
            productDtoList.add(orderReq);
        }
        return productDtoList;
    }

    @Test
    void test(){
        ProductDto sample = getSample();
        List<ProductDto> productDtoList = new ArrayList<>();
        productDtoList.add(sample);

        orderService.order(productDtoList);
    }

    @Test
    void test1(){
        executeStr();
    }

    String executeStr(){
        String a = null;
        try{
            return getNameStr();
        }finally {
            System.out.println("finally 호출");
        }
    }
    String getNameStr(){
        System.out.println("메서도 호출");
        hook();
        return "name";
    }

    void hook(){
        System.out.println("로직...");
    }

    @Test
    void 다중요청_테스트_재고수만큼주문() throws InterruptedException {
        ProductDto sample = getSample();
        List<ProductDto> productDtoList = new ArrayList<>();
        productDtoList.add(sample);

        Product product = productService.getProductById(sample.getProductId());
        int threadCount = product.getQuantity();

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    orderService.order(productDtoList);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
    }

}