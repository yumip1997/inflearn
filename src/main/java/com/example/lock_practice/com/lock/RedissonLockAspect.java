package com.example.lock_practice.com.lock;

import com.example.lock_practice.com.exception.BusinessException;
import com.example.lock_practice.com.exception.DataException;
import com.example.lock_practice.com.lock.annotation.RedissonLockAno;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.lock_practice.com.exception.message.com.CommonExceptionMessage.LOCK_ACQUIRE_FAIL;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RedissonLockAspect {

    private final RedissonClient redissonClient;
    private final PlatformTransactionManager transactionManager;

    @Pointcut("@annotation(com.example.lock_practice.com.lock.annotation.RedissonLockAno)")
    private void RedissonLockAno(){}

    @Around("RedissonLockAno() && args(arg,..)")
    public Object execute(ProceedingJoinPoint joinPoint, List<? extends LockKey> arg) throws Throwable {
        RLock multiLock = getMultiLock(getKeyList(arg));
        RedissonLockAno annotation = getAnnotation(joinPoint);

        try {
            boolean res = multiLock.tryLock(annotation.waitTime(), annotation.leaseTime(), annotation.timeUnit());
            if(!res){
                throw new DataException(LOCK_ACQUIRE_FAIL);
            }
            return executeWithTransaction(joinPoint);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
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
        return new RedissonMultiLock(rLocks);
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

    private Object executeWithTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try{
            Object result = joinPoint.proceed();
            transactionManager.commit(status);
            return result;
        }catch (Throwable e){
            transactionManager.rollback(status);
            throw e;
        }
    }
}

