package com.example.hello.apply;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class TxBasicTest {

    @Autowired
    BasicService basicService;

    @Test
    void proxyCheck(){
        log.info("aop class={}", basicService.getClass());
        Assertions.assertThat(AopUtils.isAopProxy(basicService)).isTrue();
    }

    @Test
    void txTest(){
        basicService.tx();
        basicService.notx();
    }

    @TestConfiguration
    static class TxApplyBasicConfig {

        @Bean
        BasicService basicService(){
            return new BasicService();
        }
    }

    //프록시는 클래스 단위로 만들어짐
    //tx를 호출할 떄는 당연히 프록시 객체가 호출되고, transaction의 부가기능이 더해진 tx 메서드가 호출된다.
    //notx를 호출할 때도 프록시 객체가 호출되고, 프록시 객체가 실제 객체의 noTx 메서드를 호출한다. (실제 객체에 로직 실행 위임)
    @Slf4j
    @Transactional
    static class BasicService {

        public void tx(){
            log.info("call tx");
            boolean actualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active = {}", actualTransactionActive);
        }

        public void notx(){
            log.info("call notx");
            boolean actualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active = {}", actualTransactionActive);
        }

    }
}
