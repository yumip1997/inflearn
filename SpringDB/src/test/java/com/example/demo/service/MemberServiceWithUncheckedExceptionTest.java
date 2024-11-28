package com.example.demo.service;

import com.example.demo.Repository.MemberRepository;
import com.example.demo.Repository.MemberRepositoryV3;
import com.example.demo.Repository.MemberRepositoryWithUncheckedException;
import com.example.demo.connection.ConnectionConst;
import com.example.demo.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

import static com.example.demo.connection.ConnectionConst.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class MemberServiceWithUncheckedExceptionTest {

    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberServiceWithUncheckedException memberService;

    @TestConfiguration
    @RequiredArgsConstructor
    static class TestConfig {

        @Bean
        DataSource dataSource(){
            return new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        }
        @Bean
        MemberRepository memberRepository() {
            return new MemberRepositoryWithUncheckedException(dataSource());
        }

        @Bean
        MemberServiceWithUncheckedException memberServiceWithUncheckedException() {
            return new MemberServiceWithUncheckedException(memberRepository());
        }
    }

    @AfterEach
    void after(){
        memberRepository.delete(MEMBER_A);
        memberRepository.delete(MEMBER_B);
        memberRepository.delete(MEMBER_EX);
    }

    @Test
    @DisplayName("정상이체")
    void accountTransfer() throws SQLException {
        //given
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberB = new Member(MEMBER_B, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        //when
        memberService.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);

        //then
        Member fromMember = memberRepository.findById(memberA.getMemberId());
        Member toMember = memberRepository.findById(memberB.getMemberId());
        Assertions.assertThat(fromMember.getMoney()).isEqualTo(8000);
        Assertions.assertThat(toMember.getMoney()).isEqualTo(12000);
    }

    @Test
    @DisplayName("이체 중 예외 발생")
    void accountTransferEx(){
        //given
        Member memberA = new Member(MEMBER_A, 10000);
        Member membereX = new Member(MEMBER_EX, 10000);
        memberRepository.save(memberA);
        memberRepository.save(membereX);

        //when
        Assertions.assertThatThrownBy(() -> memberService.accountTransfer(memberA.getMemberId(), membereX.getMemberId(), 2000))
                .isInstanceOf(IllegalStateException.class);

        //then
        Member fromMember = memberRepository.findById(memberA.getMemberId());
        Member toMember = memberRepository.findById(membereX.getMemberId());
        Assertions.assertThat(fromMember.getMoney()).isEqualTo(10000);
        Assertions.assertThat(toMember.getMoney()).isEqualTo(10000);
    }

    @Test
    void checkAop() {
        Assertions.assertThat(AopUtils.isAopProxy(memberService)).isTrue();
        Assertions.assertThat(AopUtils.isAopProxy(memberRepository)).isFalse();
    }

}