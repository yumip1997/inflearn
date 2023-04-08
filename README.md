# SpringDB2

## 트랜잭션 적용
### 트랜잭션 적용 특징
1. 트랜잭션 옵션 - 더 구체적이고 자세한 것이 높은 우선순위를 가진다.  
    ex. 클래스에 readonly = true , 메서드에 readonly = false
       ⇒ 메서드가 더 자세하기때문에 readonly = false 옵션이 적용됨
2. 클래스에 적용하면 메서드는 자동적용

```java
@Slf4j
    @Transactional(readOnly = true)
    static class LevelService {

        @Transactional(readOnly = false)
        public void write(){
            log.info("call write");
            printTxInfo();
        }

        public void read(){
            log.info("call read");
            printTxInfo();
        }

        private void printTxInfo(){
            boolean active = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active = {}", active);
            boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            log.info("current tx readonly = {}", readOnly);

        }
    }
```

```markdown
2023-03-30 14:19:44.671 TRACE 12164 --- [    Test worker] o.s.t.i.TransactionInterceptor           : Getting transaction for [com.hello.springtx.apply.TxLevelTest$LevelService.write]
2023-03-30 14:19:44.681  INFO 12164 --- [    Test worker] c.h.s.apply.TxLevelTest$LevelService     : call write
2023-03-30 14:19:44.681  INFO 12164 --- [    Test worker] c.h.s.apply.TxLevelTest$LevelService     : tx active = true
2023-03-30 14:19:44.681  INFO 12164 --- [    Test worker] c.h.s.apply.TxLevelTest$LevelService     : current tx readonly = false
2023-03-30 14:19:44.681 TRACE 12164 --- [    Test worker] o.s.t.i.TransactionInterceptor           : Completing transaction for [com.hello.springtx.apply.TxLevelTest$LevelService.write]
2023-03-30 14:19:44.684 TRACE 12164 --- [    Test worker] o.s.t.i.TransactionInterceptor           : Getting transaction for [com.hello.springtx.apply.TxLevelTest$LevelService.read]
2023-03-30 14:19:44.684  INFO 12164 --- [    Test worker] c.h.s.apply.TxLevelTest$LevelService     : call read
2023-03-30 14:19:44.684  INFO 12164 --- [    Test worker] c.h.s.apply.TxLevelTest$LevelService     : tx active = true
2023-03-30 14:19:44.684  INFO 12164 --- [    Test worker] c.h.s.apply.TxLevelTest$LevelService     : current tx readonly = true
2023-03-30 14:19:44.685 TRACE 12164 --- [    Test worker] o.s.t.i.TransactionInterceptor           : Completing transaction for [com.hello.springtx.apply.TxLevelTest$LevelService.read]
```

### AOP 주의 사항 - 프록시 내부 호출

문제 : 프록시 객체의 메서드가 또 다른 내부 메서드를 호출할 때, 그 내부 메서드에 @Transactional이 선언되어 있어도 트랜잭션이 적용되지 않는다.  
원인 : 호출된 내부 메서드는 실제 객체의 것이기 때문이다.  
해결방법 : 클래스를 분리한다.  

```java
static class CallService{

        public void external(){
            log.info("call external");
            printTxInfo();
            internal();
        }

        @Transactional
        public void internal(){
            log.info("call internal");
            printTxInfo();
        }

        private void printTxInfo(){
            boolean active = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active = {}", active);
            boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            log.info("current tx readonly = {}", readOnly);
        }
    }
```

```java
    @Test
    void call_external(){
        callService.external();
    }
```

```java
2023-03-30 14:46:18.248  INFO 9992 --- [    Test worker] c.h.springtx.apply.InternalCallV1Test    : call external
2023-03-30 14:46:18.248  INFO 9992 --- [    Test worker] c.h.springtx.apply.InternalCallV1Test    : tx active = false
2023-03-30 14:46:18.248  INFO 9992 --- [    Test worker] c.h.springtx.apply.InternalCallV1Test    : current tx readonly = false
2023-03-30 14:46:18.248  INFO 9992 --- [    Test worker] c.h.springtx.apply.InternalCallV1Test    : call internal
2023-03-30 14:46:18.248  INFO 9992 --- [    Test worker] c.h.springtx.apply.InternalCallV1Test    : tx active = false
2023-03-30 14:46:18.248  INFO 9992 --- [    Test worker] c.h.springtx.apply.InternalCallV1Test    : current tx readonly = false
```

1. 프록시 객체가 실제 객체의 external을 대신 호출해준다.
2. 실제 객체의 external 메서드가 호출된다.
3. 실제 객체의 internal 메서드가 호출된다. (트랜잭션 프록시 객체가 아니기에, 트랜잭션이 적용되어 있지 않다.)

## 트랜잭션 전파
- 물리 트랜잭션 : 데이터베이스에 적용되는 트랜잭션
- 논리 트랜잭션 : 트랜잭션 매너지를 통해 트랜잭션을 사용하는 단위
-> 논리 트랜잭션들은 하나의 물리 트랜잭션으로 묶인다.

- 원칙
1. 모든 논리 트랜잭션이 커밋되어야 물리 트랜잭션이 커밋된다.
2. 하나의 논리 트랜잭션이라도 롤백되면 물리 트랜잭션은 롤백된다.

- 처음 트랜잭션을 시작한 외부 트랜잭션이 실제 물리 트랜잭션을 관리하도록 한다.
1. 내부 트랜잭션 커밋 -> 외부 트랜잭션 커밋
   물리 트랜잭션은 커밋 된다. 즉, 트랜잭션의 작업 결과가 DB에 반영된다.
2. 내부 트랜잭션 커밋 -> 외부 트랜잭션 롤백
   물리 트랜잭션은 롤백 된다. 즉, 트랜잭션의 작업 결과가 DB에 반영되지 않는다.
3. 내부 트랜잭션 롤백 -> 외부 트랜잭션 커밋
   물리 트랜잭션은 롤백 된다. 즉, 트랜잭션의 작업 결과가 DB에 반영되지 않는다.
4. 내부 트랜잭션 롤백 -> 외부 트랜잭션 롤백
   물리 트랜잭션은 롤백 된다. 즉, 트랜잭션의 작업 결과가 DB에 반영되지 않는다.
   
###  REQUIRED 전파 레벨 내부 롤백, 외부 커밋 로그
```
        외부 트랜잭션 시작
        Creating new transaction with name [null]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
        Acquired Connection [HikariProxyConnection@863026773 wrapping conn0: url=jdbc:h2:mem:fbdbc658-2e98-486e-b554-b90b3c3b3f17 user=SA] for JDBC transaction
        Switching JDBC Connection [HikariProxyConnection@863026773 wrapping conn0: url=jdbc:h2:mem:fbdbc658-2e98-486e-b554-b90b3c3b3f17 user=SA] to manual commit
        
        내부 트랜잭션 시작
        Participating in existing transaction
        
        내부 트랜잭션 롤백
        Participating transaction failed - marking existing transaction as rollback-only
        Setting JDBC transaction [HikariProxyConnection@863026773 wrapping conn0: url=jdbc:h2:mem:fbdbc658-2e98-486e-b554-b90b3c3b3f17 user=SA] rollback-only
        
        외부 트랜잭션 커밋
        Global transaction is marked as rollback-only but transactional code requested commit
        Initiating transaction rollback
        Rolling back JDBC transaction on Connection [HikariProxyConnection@863026773 wrapping conn0: url=jdbc:h2:mem:fbdbc658-2e98-486e-b554-b90b3c3b3f17 user=SA]
        Releasing JDBC Connection [HikariProxyConnection@863026773 wrapping conn0: url=jdbc:h2:mem:fbdbc658-2e98-486e-b554-b90b3c3b3f17 user=SA] after transaction
```
