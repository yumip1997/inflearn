package com.example.demo.service;

import com.example.demo.Repository.MemberRepositoryV2;
import com.example.demo.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;

import static com.example.demo.connection.ConnectionConst.*;

class MemberServiceV2Test {
    public static final String MEMBER_A ="memberA";
    public static final String MEMBER_B ="memberB";
    public static final String MEMBER_EX = "ex";

    private MemberRepositoryV2 memberRepositoryV2;
    private MemberServiceV2 memberServiceV2;

    @BeforeEach
    void before(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        memberRepositoryV2 = new MemberRepositoryV2(dataSource);
        memberServiceV2 = new MemberServiceV2(dataSource, memberRepositoryV2);
    }

    @AfterEach
    void after() throws SQLException {
        memberRepositoryV2.delete(MEMBER_A);
        memberRepositoryV2.delete(MEMBER_B);
        memberRepositoryV2.delete(MEMBER_EX);
    }

    @Test
    @DisplayName("정상이체")
    void accountTransfer() throws SQLException {
        //given
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberB = new Member(MEMBER_B, 10000);
        memberRepositoryV2.save(memberA);
        memberRepositoryV2.save(memberB);

        //when
        memberServiceV2.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);

        //then
        Member fromMember = memberRepositoryV2.findById(memberA.getMemberId());
        Member toMember = memberRepositoryV2.findById(memberB.getMemberId());
        Assertions.assertThat(fromMember.getMoney()).isEqualTo(8000);
        Assertions.assertThat(toMember.getMoney()).isEqualTo(12000);
    }

    @Test
    @DisplayName("이체 중 예외 발생")
    void accountTransferEx() throws SQLException {
        //given
        Member memberA = new Member(MEMBER_A, 10000);
        Member membereX = new Member(MEMBER_EX, 10000);
        memberRepositoryV2.save(memberA);
        memberRepositoryV2.save(membereX);

        //when
        Assertions.assertThatThrownBy(() -> memberServiceV2.accountTransfer(memberA.getMemberId(), membereX.getMemberId(), 2000))
                .isInstanceOf(IllegalStateException.class);

        //then
        Member fromMember = memberRepositoryV2.findById(memberA.getMemberId());
        Member toMember = memberRepositoryV2.findById(membereX.getMemberId());
        Assertions.assertThat(fromMember.getMoney()).isEqualTo(10000);
        Assertions.assertThat(toMember.getMoney()).isEqualTo(10000);
    }
}