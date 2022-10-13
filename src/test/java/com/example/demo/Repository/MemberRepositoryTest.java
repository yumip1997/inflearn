package com.example.demo.Repository;

import com.example.demo.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class MemberRepositoryTest {

    MemberRepository memberRepository = new MemberRepository();

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