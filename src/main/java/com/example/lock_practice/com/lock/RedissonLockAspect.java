package com.example.lock_practice.com.lock;

import com.example.lock_practice.com.exception.BusinessException;
import com.example.lock_practice.com.exception.transaction.TransactionManagerHelper;
import com.example.lock_practice.com.lock.annotation.RedissonLockAno;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Aspect
@RequiredArgsConstructor
@Slf4j
public class RedissonLockAspect {

    private final RedissonClient redissonClient;
    private final TransactionManagerHelper transactionManagerHelper;

    @Pointcut("@annotation(com.example.lock_practice.com.lock.annotation.RedissonLockAno)")
    private void RedissonLockAno(){}

    @Around("RedissonLockAno() && args(arg,..)")
    public Object execute(ProceedingJoinPoint joinPoint, List<? extends LockKey> arg){
        log.info("RedissonLock Anno 시작");
        RLock multiLock = getMultiLock(getKeyList(arg));
        RedissonLockAno annotation = getAnnotation(joinPoint);

        try {
            boolean res = multiLock.tryLock(annotation.waitTime(), annotation.leaseTime(), annotation.timeUnit());
            return joinPoint.proceed();
        } catch (Throwable e){
            transactionManagerHelper.rollback();
            throw new BusinessException(e);
        } finally {
            transactionManagerHelper.commit();
            multiLock.unlock();
        }
    }

    private List<String> getKeyList(List<? extends LockKey> lockKeys){
        return lockKeys.stream()
                .map(LockKey::getKey)
                .collect(Collectors.toList());
    }

    private RLock getMultiLock(List<String> keyList) {
        RLock[] rLocks = getRLockArr(keyList);
        return redissonClient.getMultiLock(rLocks);
    }

    private RLock[] getRLockArr(List<String> keyList) {
        return keyList.stream()
                .map(redissonClient::getLock)
                .toArray(RLock[]::new);
    }

    private RedissonLockAno getAnnotation(ProceedingJoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(RedissonLockAno.class);
    }
}
