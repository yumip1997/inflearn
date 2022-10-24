package com.example.demo.exception.translator;

import com.example.demo.connection.ConnectionConst;
import com.example.demo.domain.Member;
import com.example.demo.exception.MyDBDuplicateKeyException;
import com.example.demo.exception.MyDBException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import static com.example.demo.connection.ConnectionConst.*;

@Slf4j
public class TranslatorV1Test {

    private Respository respository;
    private Service service;

    @BeforeEach
    void init(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        respository = new Respository(dataSource);
        service = new Service(respository);
    }
    @RequiredArgsConstructor
    static class Service {
        private final Respository respository;

        void create(String memberId){
            try{
                respository.save(new Member(memberId, 0));
                log.info("saveId = {}, memberId");
            }catch (MyDBDuplicateKeyException e){
                String retryId = generateNewId(memberId);
                log.info("키 중복, 복구 시도");
                respository.save(new Member(retryId, 0));
            }catch (MyDBException e){
                throw e;
            }
        }

        private String generateNewId(String memberId){
            return memberId + new Random().nextInt(1000);
        }
    }
    @RequiredArgsConstructor
    static class Respository{
        private final DataSource dataSource;

        public void save(Member member){
            String sql = "insert into member(member_id, money) values(?,?)";
            Connection con = null;
            PreparedStatement pstmt = null;

            try{
                con = dataSource.getConnection();
                pstmt = con.prepareStatement(sql);
                pstmt.setString(1, member.getMemberId());
                pstmt.setInt(2, member.getMoney());
                pstmt.executeUpdate();
            }catch (SQLException e){
                if(e.getErrorCode() == 23505){
                    throw new MyDBDuplicateKeyException(e);
                }
                throw new MyDBException(e);
            }finally {
                JdbcUtils.closeStatement(pstmt);
                JdbcUtils.closeConnection(con);
            }
        }
    }

    @Test
    void duplicateKeySave(){
        service.create("myID");
        service.create("myID");
    }
}
