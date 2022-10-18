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

### Spring의 트랜잭션
