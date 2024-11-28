package com.example.hello.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

//우선순위 : 더 자세한 것이 우선순위를 가진다.
//클래스 메서드 > 클래스 타입 > 인터페이스 메서드 > 인터페이스 타입
//클래스에 적용하면 메서드는 자동적용
//스프링 AOP 트랜잭션은 public 메서드에만 적용가능
@SpringBootTest
public class TxLevelTest {

    @Autowired
    LevelService levelService;

    @TestConfiguration
    static class TxLevelTestConfig{
        @Bean
        LevelService levelService(){
            return new LevelService();
        }
    }

    @Test
    void orderTest(){
        levelService.write();
        levelService.read();
    }

    @Slf4j
    @Transactional(readOnly = true)
    static class LevelService {

        @Transactional
        public void write(){
            log.info("call write");
            printTxInfo();
        }

        public void read(){
            log.info("call read");
            printTxInfo();
        }

        private void printTxInfo(){
            boolean actualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx activce = {}", actualTransactionActive);
            boolean currentTransactionReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            log.info("tx readonly = {}", currentTransactionReadOnly);
        }
    }
}
