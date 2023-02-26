# SpringDB2

##  REQUIRED 전파 레벨 내부 롤백, 외부 커밋 로그

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
