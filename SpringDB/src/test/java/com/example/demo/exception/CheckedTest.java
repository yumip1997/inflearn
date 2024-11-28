package com.example.demo.exception;


import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class CheckedTest {

    @Test
    void checked_catch(){
        MyService service = new MyService();
        service.callCatch();
    }

    @Test
    void checked_throw(){
        MyService service = new MyService();
        Assertions.assertThatThrownBy(service::callThrow)
                .isInstanceOf(MyCheckedException.class);
    }

    static class MyCheckedException extends Exception {
        public MyCheckedException(String message) {
            super(message);
        }
    }

    static class MyService {
        MyRepository repository = new MyRepository();

        /**
         * 예외를 잡아서 처리하는 코드
         */
        public void callCatch() {
            try {
                repository.call();
            } catch (MyCheckedException e) {
                //예외 처리 로직
                log.info("예외 처리, message = {}", e.getMessage(), e);
            }
        }

        /**
         * 예외를 던지는 코드
         */
        public void callThrow() throws MyCheckedException {
            repository.call();
        }
    }

    static class MyRepository {
        public void call() throws MyCheckedException {
            throw new MyCheckedException("ex");
        }
    }
}
