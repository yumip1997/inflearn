# SpringDB 스프링 데이터 접근 핵심 기술 강의 정리

## DB ↔ Java Application 간 Connection을 획득하는 방법

DB와 Java Application 간 Connection을 획득하는 대표적인 방법 두 가지가 있다. 먼저, DriverManager의 정적 메서드인 getConnection을 이용하는 방법이 있다. 두 번째, DataSource 인터페이스 구현체를 사용하는 것이다. 

### DriverManager 클래스
public static Connection getConnection(String url, String user, String password)

```java
Connection con1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
```

### DataSource 인터페이스

public Connection getConnection()

→ `DriverManagerDataSource`, `HikariDataSource`이 이를 구현하고 있다.

- DriverManagerDataSource 객체 생성 후 Connection 얻기

```java
//DriverManagerDataSource 객체 생성
//URL : DB서버 URL, USERNAME : 이름, PASSWORD : 패스워드
DataSoruce dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
Connection connection = dataSource.getConnection();
```

- HikariCPDataSoruce 객체 생성 후 Connection 얻기

```java
//HikariDataSource 객체 생성
//URL : DB서버 URL, USERNAME : 이름, PASSWORD : 패스워드
DataSource dataSource = new HikariDataSource();
dataSource.setJdbcUrl(URL);
dataSource.setUsername(USERNAME);
dataSource.setPassword(PASSWORD);
Connection connection = dataSource.getConnection();
```

## 트랜잭션

트랜잭션 - cud 행위들의 집합을 실행하고, 행위들의 실행 중 unchecked exception이 발생하면 모두 다 rollback시키고 정상적으로 실행 완료되면 commit 시킨다.

- rollback 되는 예외 - unchecked exception
- commit 되는 예외 - checked exception

### JDBC 트랜잭션

트랜잭션으로 묶인 cud 메서드들은 하나의 connection에서 실행되는데, 이를 위해서 cud 메서드들은 매개변수로 connection을 받아야 한다. 

1. autocommit false 설정
2. connection을 받아 cud 메서드들에 해당 connection을 넘기며 하나씩 실행
    - 예외가 발생하면 rollback한다.
    - 정상적으로 실행되면 commit한다.
3. connection을 반환한다.

```java
    public void doBizLogic() throws SQLException{
        DataSoruce dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        Connection connection = dataSource.getConnection();
        try{
            connection.setAutoCommit(false);
		**//비즈니스 로직 메서드 실행**
            connection.commit();
        }catch (Exception e){
            connection.rollback();
            throw new IllegalStateException(e);
        }finally {
            if(connection != null){
                try{
                    connection.setAutoCommit(true);
                    connection.close();
                }catch (Exception e){
                    log.info("error", e);
                }
            }
        }
    }
```

이러한 방식으로 트랜잭션 처리를 했을 경우 문제점은 다음과 같다. 먼저, 핵심 로직에 부가기능이 혼합되어 있다는 점이다. 즉, 비즈니스 메서드 안에 트랜잭션 처리 코드가 섞여있다는 것이다. 두 번째, 트랜잭션 처리가 필요한 비즈니스 로직 실행 시 위와 같은 코드가 반복된다는 것이다. 마지막으로, 트랜잭션 처리가 필요한 매서드와 불필요한 메서드를 모두 만들어야한다는 것이다. 두 메서드의 로직은 동일하지만 단지 connection을 파라미터로 받는 메서드와 그렇지 않는 메서드를 두 개 만들어야한다.

## Spring의 트랜잭션

## 자바 예외
### 예외 계층
![예외계층](https://user-images.githubusercontent.com/52367155/196892442-7c880beb-f4da-4431-a8db-567c60189ae7.png)</br>
Exception은 크게 checkd 예외와 unchecked 예외로 나뉜다. 컴파일러가 예외를 체크하느냐 그렇지 않느냐에 따라 분류되는 것이다. checked예외는 컴파일러가 예외를 체크하기에, 해당 예외를 잡아서 처리하거나 던지지 않으면 컴파일 오류가 발생한다. 반면, unchecked예외는 컴파일러가 해당 예외를 체크하지 않기 때문에 예외 처리하거나 던지지 않아도 된다. unchecked 예외에는 대표적으로 RuntionException이 있다.

### 예외 기본 규칙

예외를 다루는 규칙으로는 예외를 처리하거나 예외를 다시 던지는 방법이 있다. 예외 처리의 경우, Exception을 catch로 잡아 이를 처리하는 것이다. 예외를 던지는 경우, throws로 던지는 것이다.

### Checked 예외 vs Unchecked예외

기본적으로 Unchecked예외를 사용하는 것이 더 바람직하다. Checked 예외의 경우, 자바 어플리케이션 단에서 해결이 불가능한 경우가 대다수이며, 예외를 계속해서 던지게 되면 예외 파급의 문제가 발생하기 때문이다.

예를들어, JDBC 기술을 기반으로  DB와 Java Application 간 연동을 할 때,  SQLException이 발생하는 경우가 있다. Connection을 얻을 때 SQLException이 던져진다. 하지만 이러한 예외를 자바 어플리케이션에서 해결할 수 없다. DB 서버가 다운되어 Connection을 받아올 수 없을 때, 자바 코드로 서버 다운을 해결하기 불가능하기 때문이다. 따라서 예외 처리 보다 예외를 던져야할 것이다. Repository단에서 시작하여, Controller 단까지 예외는 throws 될 것인데, 여기서 예외 파급의 문제가 발생한다. 만약 JDBC 기술에서 JPA 기술로 변경된다면, throws된 모든 SQLException을 JPAException으로 바꿔야하기 때문이다. 즉, 예외의 의존관계때문에 OCP의 원칙이 깨지는 것이다.

한편, 비즈니스 로직 상 의도적으로 던져야 하는 예외의 경우 Checked 예외로 정의하여 개발자가 해당 예외를 처리하도록 하는 것이 . 예를 들어, 계좌 이체 실패 예외를 들 수 있다. 개발자가 실수로 해당 예외를 처리하지 않으면 계좌 이체에 실패했는데, 주문 로직이 정상적으로 수행된 것으로 판단된다거나 결제상태가 성공이라고 판단된다거나 하는 여러 예상치 못한 결과가 발생할 수 있다.

정리하자면, checked 예외의 경우, catch로 잡아 이를 해결하는 것이 대부분 불가능하다. 그래서 에외를 던질 경우 예외의 의존관계 문제가 생긴다.

### 예외 포함과 스택 트레이스
예외를 전환할 때는, 기존 예외를 포함해야 한다. 즉 예외 객체 생성시 기존 예외가 매개변수로 넣줘야한다. 그렇지 않으면 예외가 발생했을 때, 기존 예외에 대한 스택트레이스를 할 수 없다.

