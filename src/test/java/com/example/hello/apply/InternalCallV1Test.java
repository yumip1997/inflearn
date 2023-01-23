package com.example.hello.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class InternalCallV1Test {


    @Autowired
    CallService callService;

    @Test
    void printProxy(){
        log.info("callService class = {}", callService.getClass());
    }

    @Test
    void callInternal(){
        callService.callInternal();
    }

    @Test
    void callExternal(){
        callService.callExternal();
    }

    @TestConfiguration
    static class InternalCallV1testConfig {

        @Bean
        CallService callService(){
            return new CallService();
        }

    }

    @Slf4j
    static class CallService {

        public void callExternal(){
            log.info("call external");
            printTxInfo();
            callInternal();
        }

        @Transactional
        public void callInternal(){
            log.info("call internal");
            printTxInfo();
        }

        private void printTxInfo(){
            boolean actualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx activce = {}", actualTransactionActive);
        }
    }
}
