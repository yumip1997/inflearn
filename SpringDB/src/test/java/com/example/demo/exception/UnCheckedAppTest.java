package com.example.demo.exception;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

@Slf4j
public class UnCheckedAppTest {

    @Test
    void unchecked(){
        MyController controller = new MyController();
        Assertions.assertThatThrownBy(controller::request)
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void printEx(){
        MyController controller = new MyController();
        try{
            controller.request();
        }catch (Exception e){
            log.info("ex", e);
        }
    }

    @Test
    void printNotOriginalEx(){
        MyController controller = new MyController();
        try{
            controller.requestNotIncludingOriginalException();
        }catch (Exception e){
            log.info("ex", e);
        }
    }

    static class MyController {
        MyService myService = new MyService();
        public void request(){
            myService.logic();
        }

        public void requestNotIncludingOriginalException(){
            myService.logicNotIncludingOriginalException();
        }
    }

    static class MyService{
        MyRepository myRepository = new MyRepository();
        NetworkClient networkClient = new NetworkClient();

        public void logic(){
            myRepository.call();
            networkClient.call();
        }

        public void logicNotIncludingOriginalException(){
            myRepository.callNotIncludingOriginalException();
        }
    }

    static class MyRepository{
        public void call(){
            try{
                runSQL();
            }catch (SQLException e){
                throw new RuntimeSQLException(e);
            }
        }

        public void callNotIncludingOriginalException(){
            try{
                runSQL();
            }catch (SQLException e){
                throw new RuntimeSQLException();
            }
        }

        public void runSQL() throws SQLException {
            throw new SQLException("SQL Exception");
        }
    }

    static class NetworkClient {
        public void call(){
            throw new RuntimeConnectionException("네트워크 연결 실패");
        }
    }

    static class RuntimeConnectionException extends RuntimeException{
        public RuntimeConnectionException(String message) {
            super(message);
        }
    }

    static class RuntimeSQLException extends RuntimeException{

        public RuntimeSQLException() {
        }

        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }
    }


}
