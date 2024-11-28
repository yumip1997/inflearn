package com.example.demo.Repository;

import com.example.demo.domain.Member;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberRepositoryV0Test {

    MemberRepositoryV0 memberRepositoryV0 = new MemberRepositoryV0();

    @Test
    void crud() throws SQLException {
        //save
        Member member1 = new Member("member100", 1000);
        memberRepositoryV0.save(member1);

        //find
        Member member = memberRepositoryV0.findById(member1.getMemberId());
        //롬복 @Data를 통해 equals가 자동으로 override 됨
        //각각 필드 주소 값이 같으면 같다고 판단
        assertThat(member).isEqualTo(member1);

        //update
        memberRepositoryV0.update(member.getMemberId(), 2000);
        Member updatedMember = memberRepositoryV0.findById(member.getMemberId());
        assertThat(updatedMember.getMoney()).isEqualTo(2000);

        //delete
        memberRepositoryV0.delete(member.getMemberId());
        assertThatThrownBy(() -> memberRepositoryV0.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);

    }

}