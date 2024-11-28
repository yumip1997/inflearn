package com.example.hello.exception;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

//스프링 트랜잭션 AOP
//기본적으로 언체크 예외에 관련해서는 롤백함
//체크 예외에 관련해서는 커밋함 (롤백 옵션을 통해 체크 예외에서도 롤백할 수 있음)
//시스템 예외 - 복구 불가능 -> 런타임 에러라 판단 => 전제 데이터 롤백
//비즈니스 예외 - 시스템은 정상적이지만 비즈니스 상황에서 문제가 돼서 발생하는 예외 (EX. 고객 잔고가 부족해서 발생한 예외)
@SpringBootTest
public class RollbackTest {


    @Autowired
    RollbackService rollbackService;

    @Test
    void runtTimeException(){
        Assertions.assertThatThrownBy(() -> rollbackService.runtimeException())
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void checkedException(){
        Assertions.assertThatThrownBy(() -> rollbackService.checkedException())
                .isInstanceOf(MyException.class);
    }

    @Test
    void rollbackFor(){
        Assertions.assertThatThrownBy(() -> rollbackService.rollBackCheckedException())
                .isInstanceOf(MyException.class);
    }

    @TestConfiguration
    static class RollbackTestConfig {

        @Bean
        RollbackService rollbackService(){
            return new RollbackService();
        }

    }

    @Slf4j
    static class RollbackService {

        //런타임 예외 발생 : 롤백
        @Transactional
        public void runtimeException(){
            log.info("call runtimeException");
            throw new RuntimeException();
        }

        //체크 예외 발생 : 커밋
        @Transactional
        public void checkedException() throws MyException {
            log.info("call checkedException");
            throw new MyException();
        }

        @Transactional(rollbackFor = MyException.class)
        public void rollBackCheckedException() throws MyException {
            log.info("call checkedException");
            throw new MyException();
        }

    }
    static class MyException extends Exception{

    }
}
