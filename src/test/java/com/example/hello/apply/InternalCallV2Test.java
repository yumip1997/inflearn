package com.example.hello.apply;

import lombok.RequiredArgsConstructor;
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
public class InternalCallV2Test {


    @Autowired
    CallService callService;

    @Autowired
    InternalService internalService;

    @Test
    void printProxy(){
        log.info("callService class = {}", callService.getClass());
    }

    @Test
    void callInternal(){
        internalService.callInternal();
    }

    @Test
    void callExternal(){
        callService.callExternal();
    }

    @TestConfiguration
    static class InternalCallV1testConfig {

        @Bean
        InternalService internalService(){return new InternalService();}

        @Bean
        CallService callService(){
            return new CallService(internalService());
        }

    }

    @Slf4j
    @RequiredArgsConstructor
    static class CallService {

        private final InternalService internalService;

        public void callExternal(){
            log.info("call external");
            printTxInfo();
            internalService.callInternal();
        }


        private void printTxInfo(){
            boolean actualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx activce = {}", actualTransactionActive);
        }
    }

    static class InternalService {

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
