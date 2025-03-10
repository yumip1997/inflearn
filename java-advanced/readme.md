## 멀티쓰레드와 동시성
### 프로세스와 쓰레드

---

- **프로세스**
    - 실행 중인 프로그램
    - 각 프로세스는 별도의 메모리 공간을 가짐 → 다른 프로세스 간 간섭 없음
    - 하나 이상의 쓰레드를 반드시 포함
- **쓰레드**
    - 프로세스 내에서 실행되는 작업의 단위
    - 프로세스가 제공하는 메모리 공간을 공유
    - 메모리 구성:
        - 공유 메모리
        - 개별 스택: 자신만의 스택을 가짐 (각 쓰레드는 독립된 스택을 사용)
- **예시**
    - **워드 프로그램 - 프로세스 A**
        - 쓰레드1: 문서 편집
        - 쓰레드2: 자동 저장
        - 쓰레드3: 맞춤법 검사
    - **유튜브 - 프로세스 B**
        - 쓰레드1: 영상 재생
        - 쓰레드2: 댓글 처리
- **멀티 쓰레드가 필요한 이유?**
    1. **응답성**
        - 여러 사용자의 요청을 동시에 처리 가능 → 각각의 쓰레드가 각각의 요청을 처리
    2. **성능**
        - 멀티 코어 시스템에서 각 코어를 활용하여 여러 작업을 병렬 처리할 수 있음

### 쓰레드의 구성 요소

---

쓰레드는 Stack과 Instruction Pointer로 구성

- **Stack**
    - 지역 변수와 함수 호출 정보가 저장되는 메모리 영역
- **Instruction Pointer**
    - 현재 실행 중인 명령어의 주소를 가리키는 포인터

### 쓰레드 스케줄링 및 관리

---

- **Context Switching**
    - CPU는 하나의 쓰레드를 실행하고 멈춘 뒤 다른 쓰레드를 실행하는 과정을 반복한다. 이 과정을 **Context Switching**이라고 한다.
    - 컨텍스트 전환은 CPU가 현재 실행 중인 쓰레드의 상태를 저장하고, 다음 쓰레드를 실행하기 위한 상태를 복구하는 과정
- **Thrashing**
    - 동시에 많은 쓰레드를 다룰 경우, 쓰레드의 실제 작업보다 쓰레드의 관리에 더 많은 시간이 소요되는데, 이를 **Thrashing**이라 한다.
    - **Context Switching**이 너무 많아져 실제 작업을 제대로 수행하지 못하는 상황
- **Thread Scheduling**
    - 운영체제는 다양한 방식으로 쓰레드의 실행 순서를 결정
        1. **First Come First Serve (선착순)**
            - 공평하지만, 실행 시간이 긴 쓰레드가 먼저 요청되면 ‘기아 현상’(Starvation)이 발생
        2. **Shortest Job First**
            - 긴 작업들은 영원히 실행되지 않을 수 있음

  실제 운영체제는 **에포크**(Epoch) 기반으로 쓰레드들의 실행 순서를 결정하며, 각 쓰레드에 실행 시간이 할당된다. 또한, **정적 우선순위**(개발자가 설정)와 **보너스 우선순위**(운영체제가 설정)에 의해 실행 시간이 조정된다. 이를 통해 즉각적인 반응이 필요한 쓰레드에 우선순위를 부여하고, 이전 에포크에서 실행 시간이 부족한 쓰레드에 우선순위를 주어 기아 현상을 방지한다.


### 쓰레드 구현과 실행

---

- **Thread 클래스 상속**

    ```java
    class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("Thread 상속을 통한 실행");
        }
    
        public static void main(String[] args) {
            MyThread thread = new MyThread(); // Thread 기본 생성자 호출
            thread.start(); // run() 메서드 실행
        }
    }
    ```

- **Runnable 인터페이스 구현**

    ```java
    class MyRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println("Runnable 인터페이스 구현을 통한 실행");
        }
    
        public static void main(String[] args) {
            MyRunnable myRunnable = new MyRunnable();
            Thread thread = new Thread(myRunnable); // Runnable 타입을 매개변수로 받은 Thread 생성자 호출
            thread.start(); // run() 메서드 실행
        }
    }
    ```


- **start()와 run()의 차이**
    - start() : **새로운 쓰레드를 시작**하고, 새로운 호출 스택에서 run() 메서드를 실행
    - run() : **단순히 현재 쓰레드에서 실행되는 메서드**

  ⇒ Thread 인스턴스의 start() 메서드를 호출해야 해당 쓰레드가 생성되고, 운영체제의 스케쥴러에 의해 병렬 실행된다.


### 쓰레드 제어와 생명주기

---

**쓰레드 생명주기**

![life](https://github.com/user-attachments/assets/2695c378-5f33-4619-b910-bf624e40ec9f)

- **join**
    - `join()` 은 다른 쓰레드가 종료될 때까지 현재 쓰레드를 대기 상태로 만든다. 즉, `join()`을 호출한 쓰레드는 해당 쓰레드가 완료될 때까지 기다린다. 따라서, join 메서드를 사용하면 여러 쓰레드를 순차적으로 실행하거나, 특정 쓰레드의 작업이 완료되기를 기다릴 수 있다.
    - 동작 방식:
        - `join()` 메서드는 호출된 쓰레드가 완료될 때까지 현재 쓰레드의 실행을 **일시 정지**
        - `join(ms)` 메서드를 호출하면, 지정된 시간(ms) 동안만 기다리며, 그 후에는 대기 상태가 해제

  ![join](https://github.com/user-attachments/assets/2b0d92c1-a4e8-41c4-9633-8ce4620475d2)

    - 예시

        ```java
        class MyThread extends Thread {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 5; i++) {
                        System.out.println(Thread.currentThread().getName() + " - " + i);
                        Thread.sleep(1000);  // 1초간 대기
                    }
                } catch (InterruptedException e) {
                    // 무시 (실무에선 추가적인 예시 처리 필요)
                }
            }
        
            public static void main(String[] args) throws InterruptedException {
                MyThread thread1 = new MyThread();
                MyThread thread2 = new MyThread();
        
                thread1.start();
                thread2.start();
        
                thread1.join();  // thread1이 종료될 때까지 기다림
                thread2.join();  // thread2가 종료될 때까지 기다림
        
                System.out.println("모든 쓰레드가 종료되었습니다.");
            }
        }
        ```

      ⇒ 메인쓰레드는 thread1, thread2가 종료될 때까지 기다림.

- **interrupt**
    - **interrupt()** 메서드는 실행 중인 쓰레드를 중단하거나 대기 상태에서 깨우는 데 사용된다. 주로 **WAITING**, **TIMED_WAITING** 상태에 있는 쓰레드에 영향을 미친다.
    - 동작 방식:
        1. 쓰레드가 **sleep()**, **wait()**, **join()** 등의 메서드로 대기 중일 때, interrupt()를 호출하면 `InterruptedException` 예외가 발생
        2. `InterruptedException`이 발생하면 쓰레드는 **RUNNABLE** 상태로 변경되며, 이때 쓰레드는 정상적으로 종료되거나 예외 처리 등을 통해 계속 실행할 수 있다.
    - 주요 메서드:
        - `void interrupt()`: 현재 쓰레드를 인터럽트 상태로 설정
        - `boolean isInterrupted()`: 쓰레드가 인터럽트 되었는지 확인
        - `static boolean interrupted()`: 현재 쓰레드가 인터럽트 되었는지 확인 후, 상태를 초기화

- **yield**
    - **yield()** 메서드는 현재 실행 중인 쓰레드가 자발적으로 CPU 사용 기회를 다른 쓰레드에게 양보한다. 이 메서드는 **Runnable** 상태의 쓰레드에게 CPU 시간을 양보하고, 스케줄러가 다른 쓰레드를 실행할 수 있게 한다.
    - 동작 방식:
        - `Thread.yield()`를 호출하면 현재 실행 중인 쓰레드는 **RUNNABLE** 상태로 돌아가고, 다른 쓰레드에게 CPU를 양보
        - 단, `yield()`가 호출된다고 해서 반드시 다른 쓰레드가 실행되는 것은 아니며, 운영체제의 스케줄러에 의해 CPU가 다른 쓰레드에 할당될 수도 있고, 현재 쓰레드가 계속 실행될 수도 있음

- **volatile**
    - 메모리 가시성 문제
        - 한 쓰레드에서 변경한 변수 값이 다른 쓰레드에 즉시 반영되지 않는 문제
    - 왜 발생할까?
        - 자바의 성능 최적화로 인해, 각 쓰레드는 자신의 **CPU 캐시**를 사용한다. 이로 인해 한 쓰레드에서 변수 값을 변경해도, 변경된 값이 즉시 메인 메모리(Heap)에 반영되지 않으면 다른 쓰레드는 변경된 값을 볼 수 없다.
    - 해결책
        - 여러 쓰레드에서 같은 변수의 값을 읽고 써야한다면 volatile 키워드를 사용하면 된다.
    - volatile
        - 변수를 메인 메모에서 직접 읽고 쓰도록 강제하는 역할을 한다.
        - CPU 캐시를 거치지 않고, 항상 메인 메모리에서 읽고 쓰도록 하기에 서능 저하가 발생할 수 있다.

      ⇒ 가시성이 꼭 필요한 경우 알맞은 변수에만 volatile을 써야한다.

### 동기화
---
- 동시성 문제

  공유 자원에 여러 쓰레드가 동시에 접근할 때 발생하는 문제  
    ex. 공유될 수 있는 자원 : 인스턴스 변수, 클래스 변수

- 동시성 문제 예시

  1000원이 남아있는 계좌에서 두 개의 쓰레드가 800원을 출금하려는 작업을 시작   
  → 최종적으로 계좌에는 -600원 또는 800원이 두 번 출금되었는데도 200이 남는다.

    ```java
    public class Account {
    
        private int balance;
    
        public Account(int balance) {
            this.balance = balance;
        }
    
        public void withdraw(int amount) {
            System.out.println("["+Thread.currentThread().getName()+"] 출금을 시작합니다. 현재 잔액: " + balance);
            if(balance < amount) {
                System.out.println("["+Thread.currentThread().getName()+"] 출금에 실패했습니다. (잔액이 부족)");
                return;
            }
    
            System.out.println("["+Thread.currentThread().getName()+"] 출금을 시도합니다.");
            balance -= amount;
            System.out.println("["+Thread.currentThread().getName()+"] 출금에 성공했습니다. 현재 잔액: " + balance);
        }
    
        public int getBalance() {
            return balance;
        }
    
        public static void main(String[] args) throws InterruptedException {
            Account bankAccount = new Account(1000);
    
            Thread t1 = new Thread(() -> bankAccount.withdraw(800), "T1");
            Thread t2 = new Thread(() -> bankAccount.withdraw(800), "T2");
    
            t1.start();
            t2.start();
    
            t1.join();
            t2.join();
    
            System.out.println("최종 잔액 : " + bankAccount.getBalance());
        }
    }
    
    ```

  CASE1 - T1 쓰레드 → T2 쓰레드

    - 거의 동시에 if 조건을 확인하기에, 조건문을 빠져나간다.
    - T1이 먼저 balance의 값을 200원으로 만든다.
    - T2가 200인 balance에 다시 800원을 차감한다 → balance의 값은 -600이 된다.

  CASE2 - 두 쓰레드가 동시에 실행될 떄

    - T1/T2 동시에 balance를 1000으로 읽는다
    - T1/T2 동시에 balance = balance - 200 로직 수행 → balance의 값은 200원이 된다
- 임계영역  
  한 번에 하나의 쓰레드만 접근할 수 있도록 보호된 코드 영역
- 해결 방법
  - synchronized 키워드 사용
    - synchronized 블록에 진입하기 위해서는 해당 객체의 락(Lock) 을 획득한다. 
    - 이는 한 번에 하나의 쓰레드만 임계 영역에 접근할 수 있도록 보장하여 동시성 문제를 해결할 수 있다.  
      💡 모든 객체(인스턴스)는 내부에 자신만의 모니터 락을 가진다.  
      💡 synchronized 블록에 진입하기 위해서는 해당 객체의 락을 획득해야 한다.
    - 과정
      1. 첫 번째로 도착한 쓰레드가 락을 획득
      2. 나머지 쓰레드는 락을 획득할 때까지 대기 상태로 전환
      3. 락을 보유한 쓰레드가 synchronized 블록을 빠져나가면서 락을 반환
      4. 대기 중인 쓰레드 중 하나가 자동으로 락을 획득  
      => 위 과정을 반복하며 한 번에 하나의 쓰레드만 접근할 수 있도록 보장
- 사용법
    - 메서드 : 메서드 전체 영역을 지정 (동기화가 불필요한 부분에도 지정될 수 있음)

        ```java
        public synchronized void withdraw(int amount) {
            if (balance >= amount) balance -= amount;
        }
        ```
    - 블록 : 동기화가 필요한 부분만 지정할 수 있음

        ```java
        public void withdraw(int amount) {
            synchronized (this) { 
                if (balance >= amount) balance -= amount; 
            }
        }
        ```
   
### 고급 동기화
### 고급 동기화
LockSupport, ReentrantLock => 기존 synchronizsd의 단점을 보안한 고급 동기화 기법
- synchronized의 단점
    - 무한대기: 락을 획득할때까지 BLOCKED 상태로 무기한 대기  -> interrupt()를 호출해도 락을 획득하지 않는 이상 BLOCKED 상태를 벗어날 수 없다.
    - 공정성: 락이 해제된 후 어떤 쓰레드가 락을 획득할지 예측X -> 특정 쓰레드가 계속 락을 획득하지 못할 수 있음
    - 특정 쓰레드를 깨우는 기능 부재

1. LockSupport  
   LockSupport는 Java 1.5에서 추가된 동기화 유틸리티로, 기존 synchronized의 단점을 해결하고 보다 효율적인 스레드 제어 가능
- LockSupport의 개선점
    - 무한대기 문제 해결
        - `parkNanos(long nanos)`를 사용하면 타임아웃을 설정할 수 있어 무한 대기를 방지
        - `park()` 호출 시 쓰레드는 WAITING 또는 TIMED_WAITING 상태에 들어감 -> interrupt()를 호출하여 RUNNABLE 상태로 전환 가능
    - 특정 쓰레드 깨우기
        - `unpark(Thread t)`를 통해 대기 중인 특정 쓰레드를 깨울 수 있음
- 주요 메서드
    - `park()` : 현재 쓰레드를 WAITING 상태로 전환하여 대기 시킴
    - `parkNanos(long nanos)`: 현재 쓰레드를 지정된 나노초 동안만 대기시키면 대기 동안 TIMED_WAITING 상태로 전환됨
    - `unpark(Thread t)`: park()로 대기 중인 특정 스레드를 깨워 RUNNABLE 상태로 만듦

2. ReentrantLock  
   ReentrantLock은 Java 1.5에 도입된 명시적 락 매커니즘으로 유연한 동기화 방법 제공
- ReentrantLock의 개선점
    - 공정성 문제 해결: 공정 모드 지원 -> 먼저 대기한 쓰레드가 먼저 락을 획득
        - 공정모드의 특징
            - 공정성 보장
            - 기아 현상 방지: 모든 쓰레드가 언제가 락을 획득할 수 있도록 보장
            - 성능 저하: 락을 획득하는 속도가 느려질 수 잇음
- 주요 메서드
    - `tryLock()`: 락 획득을 시도하고 즉시 성공 여부를 반환
    - `tryLock(long time, TimeUnit unit)`: 지정된 시간 동안만 락 획득을 기다림
    - `unlock()`: 락을 해제함

### 생산자 소비자 문제
- 기본 개념
  - 생산자: 데이터를 생성하여 공유 버퍼에 넣는 스레드
  - 소비자: 공유 버퍼에서 데이터를 꺼내서 사용하는 스레드
  - 버퍼: 생산자와 소비자 간 데이터를 주고받기 위한 저장소로 크기가 제한됨
- 문제상황
  - 생상자가 너무 빠를 때: 너무 빨리 생산하여 버퍼가 가득 차면 더 이상 데이터를 넣을 수 없게 됨 -> 대기
  - 소비자가 너무 빠를 때:: 버퍼가 비어 있으면 소비자가 데이터를 꺼낼 수 없게 됨 -> 대기
- 생산자 소비자 문제 
  - 생산자 스레드와 소비자 스레드가 특정 자원을 함께 생산하고 소비하면서 발생하는 문제
  - 버퍼의 크기가 한정되어 있기에 해당 문제가 발생하기에 '한정된 버퍼 문제'라고도 함