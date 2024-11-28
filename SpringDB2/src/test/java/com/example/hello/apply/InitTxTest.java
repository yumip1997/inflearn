package com.example.hello.apply;

import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringBootTest
@Slf4j
public class InitTxTest {

    @Autowired Hello hello;

    @Test
    void go(){

    }

    @TestConfiguration
    static class InitTxConfig {

        @Bean
        Hello hello(){
            return new Hello();
        }
    }
    static class Hello {

        @PostConstruct
        @Transactional
        public void initV1(){
            boolean actualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("Hello init @PostConstruct tx active = {}", actualTransactionActive);
        }

        @EventListener(ApplicationReadyEvent.class)
        @Transactional
        public void initV2(){
            boolean actualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("Hello init @ApplicationReadyEvent tx active = {}", actualTransactionActive);
        }
    }
}
