package com.example.demo.service;

import com.example.demo.Repository.MemberRepositoryV2;
import com.example.demo.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {

    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepositoryV2;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Connection connection = dataSource.getConnection();
        try{
            //자동 커밋 모드를 수동 커밋 모드로 수행 -> 트랜잭션 시작!
            connection.setAutoCommit(false);
            doBusinessLogic(connection, fromId, toId, money);
            connection.commit();
        }catch (Exception e){
            connection.rollback();
            throw new IllegalStateException(e);
        }finally {
            release(connection);
        }
    }

    private void doBusinessLogic(Connection connection, String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepositoryV2.findById(connection, fromId);
        Member toMember = memberRepositoryV2.findById(connection, toId);

        memberRepositoryV2.update(connection, fromId, fromMember.getMoney() - money);
        validate(toMember);
        memberRepositoryV2.update(connection, toId, toMember.getMoney() + money);
    }

    private void release(Connection connection) {
        if(connection != null){
            try{
                connection.setAutoCommit(true);
                connection.close();
            }catch (Exception e){
                log.info("error", e);
            }
        }
    }

    private void validate(Member toMember) {
        if(toMember.getMemberId().equals("ex")){
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
