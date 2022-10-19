package com.example.demo.exception;


import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

@Slf4j
public class CheckedAppTest {

    @Test
    void checked(){
        MyController controller = new MyController();
        Assertions.assertThatThrownBy(controller::request)
                .isInstanceOf(Exception.class);
    }
    static class MyController {
        MyService service = new MyService();

        public void request() throws SQLException, ConnectException {
            service.logic();
        }
    }
    static class MyService{
        MyRepository repository = new MyRepository();
        NetWorkClient netWorkClient = new NetWorkClient();

        public void logic() throws SQLException, ConnectException {
            repository.call();
            netWorkClient.call();
        }

    }

    static class NetWorkClient {
        public void call() throws ConnectException {
            throw new ConnectException("연결 실패");
        }
    }

    static class MyRepository{
        public void call() throws SQLException {
            throw new SQLException("ex");
        }
    }
}
