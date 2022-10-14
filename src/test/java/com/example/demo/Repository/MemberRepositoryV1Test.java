package com.example.demo.Repository;

import com.example.demo.connection.ConnectionConst;
import com.example.demo.domain.Member;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static com.example.demo.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class MemberRepositoryV1Test {

    MemberRepositoryV1 memberRepository;

    @BeforeEach
    void beforeEach(){
//        //기본 DriverManager - 항상 새로운 커넥션 획득 -> 성능이 느림!
//        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
//        memberRepository = new MemberRepositoryV1(dataSource);

        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(URL);
        hikariDataSource.setUsername(USERNAME);
        hikariDataSource.setPassword(PASSWORD);

        memberRepository = new MemberRepositoryV1(hikariDataSource);
    }

    @Test
    void crud() throws SQLException {
        //save
        Member member1 = new Member("member100", 1000);
        memberRepository.save(member1);

        //find
        Member member = memberRepository.findById(member1.getMemberId());
        //롬복 @Data를 통해 equals가 자동으로 override 됨
        //각각 필드 주소 값이 같으면 같다고 판단
        assertThat(member).isEqualTo(member1);

        //update
        memberRepository.update(member.getMemberId(), 2000);
        Member updatedMember = memberRepository.findById(member.getMemberId());
        assertThat(updatedMember.getMoney()).isEqualTo(2000);

        //delete
        memberRepository.delete(member.getMemberId());
        assertThatThrownBy(() -> memberRepository.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);

    }


}