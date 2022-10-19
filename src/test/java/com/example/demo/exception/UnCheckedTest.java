package com.example.demo.exception;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class UnCheckedTest {

    @Test
    void unchecked_catch(){
        MyService myService = new MyService();
        myService.callCatch();
    }

    @Test
    void unchecked_throw(){
        MyService myService = new MyService();
        Assertions.assertThatThrownBy(myService::callThrow)
                .isInstanceOf(MyUncheckedException.class);

    }
    static class MyUncheckedException extends RuntimeException{
        public MyUncheckedException(String message) {
            super(message);
        }
    }

    static class MyRepository{
        public void call(){
            throw new MyUncheckedException("unchecked exception");
        }
    }

    static class MyService{
        MyRepository myRepository = new MyRepository();

        public void callCatch(){
            try{
                myRepository.call();
            }catch (MyUncheckedException e){
                log.info("예외 처리 message = {}", e.getMessage(), e);
            }
        }

        public void callThrow(){
            myRepository.call();
        }
    }


}
