package com.example.demo.service;

import com.example.demo.Repository.MemberRepository;
import com.example.demo.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class MemberServiceWithUncheckedException {

    private final MemberRepository memberRepository;


    @Transactional
    public void accountTransfer(String fromId, String toId, int money){
        doBusinessLogic(fromId, toId,money);
    }

    private void doBusinessLogic(String fromId, String toId, int money){
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney() - money);
        validate(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);
    }

    private void validate(Member toMember) {
        if(toMember.getMemberId().equals("ex")){
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
