package com.example.hello.order;

//체크예외
//스프링 AOP 트랜잭션에서는 체크 예외가 발생하면 기본적으로 롤백하지 않는다.
public class NotEnoughMoneyException extends Exception{

    public NotEnoughMoneyException(String message) {
        super(message);
    }

}
