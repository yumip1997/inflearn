package com.example.hello.propagation;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import javax.sql.DataSource;

@Slf4j
@SpringBootTest
public class BasicTxText {

    @Autowired
    PlatformTransactionManager txManager;

    @TestConfiguration
    static class Config {

        @Bean
        public PlatformTransactionManager transactionManager(DataSource dataSource){
            return new DataSourceTransactionManager(dataSource);
        }

    }

    @Test
    void commit(){
        log.info("트랜잭션 시작");
        TransactionStatus transaction = txManager.getTransaction(new DefaultTransactionAttribute());

        log.info("트랜잭션 커밋 시작");
        txManager.commit(transaction);
        log.info("트랜잭션 커밋 완료");
    }

    @Test
    void rollback(){
        log.info("트랜잭션 시작");
        TransactionStatus transaction = txManager.getTransaction(new DefaultTransactionAttribute());

        log.info("트랜잭션 롤백 시작");
        txManager.rollback(transaction);
        log.info("트랜잭션 롤백 완료");
    }

    @Test
    void double_commit(){
        log.info("트랜잭션1 시작");
        TransactionStatus transaction1 = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("transaction1.isNewTransaction()={}", transaction1.isNewTransaction());

        log.info("트랜잭션1 커밋 시작");
        txManager.commit(transaction1);
        log.info("트랜잭션1 커밋 완료");
        // 커밋이 완료되면 커넥션 반환

        log.info("트랜잭션2 시작");
        TransactionStatus transaction2 = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("transaction1.isNewTransaction()={}", transaction2.isNewTransaction());

        log.info("트랜잭션2 커밋 시작");
        txManager.commit(transaction2);
        log.info("트랜잭션2 커밋 완료");
    }

    @Test
    void double_commit_rollback(){
        log.info("트랜잭션1 시작");
        TransactionStatus transaction1 = txManager.getTransaction(new DefaultTransactionAttribute());

        log.info("트랜잭션1 커밋 시작");
        txManager.commit(transaction1);
        log.info("트랜잭션1 커밋 완료");

        log.info("트랜잭션2 시작");
        TransactionStatus transaction2 = txManager.getTransaction(new DefaultTransactionAttribute());

        log.info("트랜잭션2 롤백 시작");
        txManager.rollback(transaction2);
        log.info("트랜잭션2 롤백 완료")  ;
    }


    @Test
    void outer_rollback(){
        log.info("외부 트랜잭션 시작");
        TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());

        log.info("내부 트랜잭션 시작");
        TransactionStatus inner = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("내부 트랜잭션 커밋");
        txManager.commit(inner);

        log.info("외부 트랜잭션 롤백");
        txManager.rollback(outer);
    }

    @Test
    void inner_rollback(){
        log.info("외부 트랜잭션 시작");
        TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());

        log.info("내부 트랜잭션 시작");
        TransactionStatus inner = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("내부 트랜잭션 롤백"); //rollback only 마킹 - rollback 되어야한다는 표시
        txManager.rollback(inner);
        // UnexpectedRollbackException 예외 발생 : Transaction rolled back because it has been marked as rollback-only
        // 트랜잭션 매니저는 신규 트랜잭션 여부에 따라 다르게 동작 -> inner 신규 트랜잭션이 아니기 때문에 실제 롤백이 일어나지 않음
        // 대신 트랜잭션 동기화 매니저에 rollbackOnly = true 라는 표시를 해둔다.

        log.info("외부 트랜잭션 커밋");
        //commit 전, 트랜잭션 동기화 매니저의 rollbackOnly = true 인지 확인
        //true 라면 commit 되지 않고 rollback 된다.
        Assertions.assertThatThrownBy(() -> txManager.commit(outer)).isInstanceOf(UnexpectedRollbackException.class);
    }

    @Test
    void inner_commit(){
        log.info("외부 트랜잭션 시작");
        TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("outer.isNewTransaction()={}", outer.isNewTransaction());

        log.info("내부 트랜잭션 시작");
        TransactionStatus inner = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("inner.isNewTransaction()={}", inner.isNewTransaction());

        log.info("내부 트랜잭션 커밋");
        txManager.commit(inner);
        // 참여한 트랜잭션의 경우 즉, 내부 트랜잭션의 경우 실제 DB 커밋은 일어나지 않음

        log.info("외부 트랜잭션 커밋");
        txManager.commit(outer);
        // 실제 DB 커밋이 일어난다.
    }

    @Test
    void test(){
        log.info("외부 트랜잭션 시작");
        TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());

        log.info("내부 트랜잭션 시작");
        TransactionStatus inner = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("내부 트랜잭션 롤백"); //rollback only 마킹 - rollback 되어야한다는 표시
        txManager.rollback(inner);
        // UnexpectedRollbackException 예외 발생 : Transaction rolled back because it has been marked as rollback-only
        // 트랜잭션 매니저는 신규 트랜잭션 여부에 따라 다르게 동작 -> inner 신규 트랜잭션이 아니기 때문에 실제 롤백이 일어나지 않음
        // 대신 트랜잭션 동기화 매니저에 rollbackOnly = true 라는 표시를 해둔다.

        log.info("외부 트랜잭션 커밋");
        //commit 전, 트랜잭션 동기화 매니저의 rollbackOnly = true 인지 확인
        //true 라면 commit 되지 않고 rollback 된다.
        Assertions.assertThatThrownBy(() -> txManager.commit(outer)).isInstanceOf(UnexpectedRollbackException.class);
    }

    @Test
    void inner_rollback_requires_new(){
        log.info("외부 트랜잭션 시작");
        TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("outer.isNewTransaction() = {}", outer.isNewTransaction());

        log.info("내부 트랜잭션 시작");
        DefaultTransactionAttribute defaultTransactionAttribute = new DefaultTransactionAttribute();
        defaultTransactionAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus inner = txManager.getTransaction(defaultTransactionAttribute);    // Suspending current transaction, creating new transaction -> 커넥션 획득, 수동 커밋
        log.info("inner.isNewTransaction() = {}", inner.isNewTransaction());

        log.info("내부 트랜잭션 롤백");
        txManager.rollback(inner);  //커넥션 반환

        log.info("외부 트랜잭션 커밋");
        txManager.commit(outer);
    }

}


