package com.example.demo.connection;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ConnectionUtilTest {

    @Test
    void connection(){
        //h2 드라이버가 제공하는 Connection구현체인 JdbcConnection이 반환
        Connection connection = DBConnectionUtil.getConnection();
        assertThat(connection).isNotNull();
    }
}
