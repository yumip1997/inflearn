package com.example.hello.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    LogRepository logRepository;

    /**
     * memberService @Transaction : OFF
     * memberRepository @Transaction : ON
     * logRepository @Transaction : ON
     */
    @Test
    void outerTxOff_success(){
        String username = "outerTxOff_success";

        memberService.joinV1(username);
        // 트랜잭션 A 시작 (커넥션 획득 -> 수동 커밋 모드 변경 -> 로직 수행 -> 커밋 -> 트랜잭션 종료 (커넥션 반환))
        // memberRepository.save()
        // 트랜잭션 B 시작 (커넥션 획득 -> 수동 커밋 모드 변경 -> 로직 수행 -> 커밋 -> 트랜잭션 종료 (커넥션 반환))
        // logRepository.save()

        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isPresent());
    }

    /**
     * memberService @Transaction : OFF
     * memberRepository @Transaction : ON
     * logRepository @Transaction : ON -> RuntimeException
     */
    @Test
    void outerTxOff_fail(){
        String username = "로그 예외_outerTxOff_success";

        Assertions.assertThrows( RuntimeException.class, () -> memberService.joinV1(username));
        // 트랜잭션 A 시작 (커넥션 획득 -> 수동 커밋 모드 변경 -> 로직 수행 -> 커밋 -> 트랜잭션 종료 (커넥션 반환))
        // memberRepository.save()
        // 트랜잭션 B 시작 (커넥션 획득 -> 수동 커밋 모드 변경 -> 로직 수행 -> 롤백 -> 트랜잭션 종료 (커넥션 반환))
        // logRepository.save()

        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isEmpty());
    }

    /**
     * memberService @Transaction : ON
     * memberRepository @Transaction : OFF
     * logRepository @Transaction : OFF
     */
    @Test
    void outerTxOn_success(){
        String username = "outerTxOff_success";

        memberService.joinV1(username);
        // 트랜잭션 A 시작 (커넥션 획득 -> 수동 커밋 모드 변경 -> 로직 수행 -> 커밋 -> 트랜잭션 종료 (커넥션 반환))
        // memberRepository.save()
        // 트랜잭션 B 시작 (커넥션 획득 -> 수동 커밋 모드 변경 -> 로직 수행 -> 커밋 -> 트랜잭션 종료 (커넥션 반환))
        // logRepository.save()

        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isPresent());
    }

    /**
     * memberService @Transaction : ON
     * memberRepository @Transaction : OFF
     * logRepository @Transaction : OFF -> RuntimeException
     */
    @Test
    void outerTxOn_fail(){
        String username = "로그 예외_outerTxOff_success";

        Assertions.assertThrows(RuntimeException.class, () -> memberService.joinV1(username));
        // 트랜잭션 A 시작 (커넥션 획득 -> 수동 커밋 모드 변경 -> 로직 수행 -> 커밋 -> 트랜잭션 종료 (커넥션 반환))
        // memberRepository.save()
        // 트랜잭션 B 시작 (커넥션 획득 -> 수동 커밋 모드 변경 -> 로직 수행 -> 롤백 -> 트랜잭션 종료 (커넥션 반환))
        // logRepository.save()

        Assertions.assertTrue(memberRepository.find(username).isEmpty());
        Assertions.assertTrue(logRepository.find(username).isEmpty());
    }
}