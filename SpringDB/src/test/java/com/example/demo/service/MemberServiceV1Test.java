package com.example.demo.service;

import com.example.demo.Repository.MemberRepositoryV1;
import com.example.demo.connection.ConnectionConst;
import com.example.demo.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;

import static com.example.demo.connection.ConnectionConst.*;
import static org.junit.jupiter.api.Assertions.*;

/*
기본동작
 */
class MemberServiceV1Test {

    public static final String MEMBER_A ="memberA";
    public static final String MEMBER_B ="memberB";
    public static final String MEMBER_EX = "ex";

    private MemberRepositoryV1 memberRepositoryV1;
    private MemberServiceV1 memberServiceV1;

    @BeforeEach
    void before(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        memberRepositoryV1 = new MemberRepositoryV1(dataSource);
        memberServiceV1 = new MemberServiceV1(memberRepositoryV1);
    }

    @AfterEach
    void after() throws SQLException {
        memberRepositoryV1.delete(MEMBER_A);
        memberRepositoryV1.delete(MEMBER_B);
    }

    @Test
    @DisplayName("정상이체")
    void accountTransfer() throws SQLException {
        //given
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberB = new Member(MEMBER_B, 10000);
        memberRepositoryV1.save(memberA);
        memberRepositoryV1.save(memberB);

        //when
        memberServiceV1.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);

        //then
        Member fromMember = memberRepositoryV1.findById(memberA.getMemberId());
        Member toMember = memberRepositoryV1.findById(memberB.getMemberId());
        Assertions.assertThat(fromMember.getMoney()).isEqualTo(8000);
        Assertions.assertThat(toMember.getMoney()).isEqualTo(12000);
    }

    @Test
    @DisplayName("이체 중 예외 발생")
    void accountTransferEx() throws SQLException {
        //given
        Member memberA = new Member(MEMBER_A, 10000);
        Member membereX = new Member(MEMBER_EX, 10000);
        memberRepositoryV1.save(memberA);
        memberRepositoryV1.save(membereX);

        //when
        Assertions.assertThatThrownBy(() -> memberServiceV1.accountTransfer(memberA.getMemberId(), membereX.getMemberId(), 2000))
                .isInstanceOf(IllegalStateException.class);

        //then
        Member fromMember = memberRepositoryV1.findById(memberA.getMemberId());
        Member toMember = memberRepositoryV1.findById(membereX.getMemberId());
        Assertions.assertThat(fromMember.getMoney()).isEqualTo(8000);
        Assertions.assertThat(toMember.getMoney()).isEqualTo(10000);
    }

}