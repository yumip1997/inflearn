# Reddison Lock 기반 재고 관리 프로그램

## 개요

멀티 쓰레드 환경에서 동기화가 제대로 되지 않으면 데이터의 정합성이 깨진다. 여러 쓰레드가 공유자원에 동시에 접근하는 상황이 발생하여 실행 순서에 따라 결과 값이 바뀌기 때문이다. 이러한 상황을 Race Condition이라 한다. Race Condition를 방지 하기 위해, 여러 쓰레드가 동시에 공유 자원에 접근하지 못하도록 하는 방법이 있다. 이를 lock을 건다고 표현한다.
<br>Redisson은 분산락 기능을 제공하여 동기화 처리를 가능하게 한다. 공통 락 저장소를 사용하여, 다수의 서버에서도 동기화가 되도록 한다. 또한 Redisson은 락의 획득과 반환 시 pub-sub 방식을 사용한다. 락이 해제될 때 subscribe하는 클라이언트는 락 획득이 가능하다는 알림을 받고, 락 획득을 시도하는 것이다. 이러한 방식은 락 획득에 성공할때까지 Redis 서버에 요청을 보내는 스핀락 방식보다 극적으로 Redis 서버에 가는 부하를 줄일 수 있다. 

## 프로젝트 설계

- 기술 스택
    
    Spring Boot, Java 8, JPA, H2 DataBase, Redis, Redisson API
    
- 모듈
    
    크게 Stock과 공통 모듈로 이루어져있다. Stock 모듈은 재고 차감 로직을 맡고 있고, Lock 획득과 반환 로직을 Aspect로 만들어 Stock 모듈 뿐 아니라 향후 동시성 제어가 필요한 다른 모듈에서도 사용할 수 있도록 공통 모듈에 포함시켰다.
    

### Lock 처리 (lock / unlock) Aspect

```java
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RedissonLockAspect {

    private final RedissonClient redissonClient;
    private final PlatformTransactionManager transactionManager;

    @Pointcut("@annotation(com.example.shoppingmall.com.lock.annotation.RedissonLockAno)")
    private void RedissonLockAno(){}

    @Around("RedissonLockAno() && args(arg,..)")
    public Object execute(ProceedingJoinPoint joinPoint, List<? extends LockKey> arg) throws Throwable {
        RLock multiLock = getMultiLock(getKeyList(arg));
        RedissonLockAno annotation = getAnnotation(joinPoint);

        try {
            boolean res = multiLock.tryLock(annotation.waitTime(), annotation.leaseTime(), annotation.timeUnit());
            if(!res){
                throw new DataException(CommonExceptionMessage.LOCK_ACQUIRE_FAIL);
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
```

- 어노테이션 기반 포인트 컷을 지정했다.
- executeWithTransaction : unlock 하기 전 commit이 먼저 되는 로직을 담고 있다.
    
    락이 해제되고나서 트랜잭션 commit이 된다면 문제가 발생될 수 있다. 다른 쓰레드에서 commit 되기 전의 데이터 값을 읽어 update 처리를 한다면 예상과는 다른 결과가 발생할 수 있기 때문이다. 예를 들어, 재고가 10개 남아있는 상황에서 쓰레드 A와 B가 재고 차감 로직을 실행하고 있는 상황이 있다. 쓰레드 A가 10개라는 재고를 읽고, 1개를 차감하고 unlock를 하고 최종적으로 commit하기 전, 쓰레드 B가 읽은 재고 값은 여전히 10개이다. 쓰레드 B가 unlock를 하고 commit하고 쓰레드 A도 최종적으로 commit하면 재고 값은 8개가 아니라 9개인 결과가 발생한다. 따라서 unlock을 하기 전 commit을 먼저 해주어야 한다. 
    

## 테스트

상황 :  재고가 45개인 상품에 대해, 주문수량은 1개씩 총 45번 주문을 한다. (총 45번의 재고차감 요청)

### Redisson Lock을 적용하지 않았을 경우

```java
    @Test
    @DisplayName("동시성 제어가 안되어있는 경우, 재고 수 만큼의 다중 쓰레드가 각각 1개씩 상품을 주문했을 때, 재고는 0개가 아니다.")
    void 동기화_되지_않은_경우() throws InterruptedException {
        OrderProductDto sample = jsonReaderUtils.getObject("/OrderSample.json", OrderProductDto.class);
        List<OrderProductDto> orderProductDtoList = new ArrayList<>();
        orderProductDtoList.add(sample);

        Product product = productService.getProductById(sample.getProductId());
        int threadCount = product.getQuantity();
        StockService stockService = new StockService(productService);

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockService.decreaseStockList(orderProductDtoList);
                }catch (SoldOutException e){
                    throw e;
                } catch (Exception e){
                    throw new RuntimeException(e);
                }finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Product updatedProduct = productService.getProductById(sample.getProductId());
        Assertions.assertThat(updatedProduct.getQuantity()).isNotEqualTo(0);
    }
```
동기화가 되지 않았기에, Race Condition이 발생하여 정상적으로 재고 차감이 이루어 지지 않는다.

실제 테스트를 여러 번 실행했을 때마다 재고가 39, 40, 43 이런식으로 다르게 나온다.

### Redisson Lock을 적용했을 경우

```java
    @Test
    @DisplayName("재고 수 만큼의 다중 쓰레드가 각각 1개씩 상품을 주문했을 경우, 재고는 0개이다.")
    void 락을_이용해_동기화_처리가_된_경우() throws InterruptedException {
        OrderProductDto sample = jsonReaderUtils.getObject("/OrderSample.json", OrderProductDto.class);
        List<OrderProductDto> orderProductDtoList = new ArrayList<>();
        orderProductDtoList.add(sample);

        Product product = productService.getProductById(sample.getProductId());
        int threadCount = product.getQuantity();

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockService.decreaseStockList(orderProductDtoList);
                }catch (SoldOutException e){
                    throw e;
                } catch (Exception e){
                    throw new RuntimeException(e);
                }finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Product updatedProduct = productService.getProductById(sample.getProductId());
        Assertions.assertThat(updatedProduct.getQuantity()).isEqualTo(0);
    }
```

동기화 처리가 되었기에, 정상적으로 재고가 차감된다.
