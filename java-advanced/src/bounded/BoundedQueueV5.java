package bounded;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static util.MyLogger.log;

public class BoundedQueueV5 implements BoundedQueue {

    private final Lock lock = new ReentrantLock();
    private final Condition producercondition = lock.newCondition(); // 쓰레드 대기공간 생성
    private final Condition consumerCondition = lock.newCondition(); // 소비자 대기공간 생성

    private final Queue<String> queue = new ArrayDeque<>();
    private final int maxSize;

    public BoundedQueueV5(int maxSize) {
        this.maxSize = maxSize;
    }

    // 생산자 -> queue의 데이터를 넣는다
    // 임계 영역: synchronized 안 붙이면 maxSize를 넘길 수도 있다! => 한 번에 하나의 쓰레드만 접근해야하는 영역
    @Override
    public void put(String data) {
        lock.lock();
        try{
            while(queue.size() == maxSize) {
                log("[put] 큐가 가득 참, 생산자 잠시 대기" + data);
                try {
                    producercondition.await();  // 생산자용 대기공간에서 생산자를 대기 상태로 보관
                    log("[put] 생산자 깨어남");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }


            queue.offer(data);
            log("[put] 생산자 데이터 저장, 소비자 대기공간에서 소비자를 깨우기 위해 singal() 호출");
            consumerCondition.signal(); // 소비자 대기 공간에서 소비자를 깨움
        }finally {
            lock.unlock();
        }
    }

    // 소비자 -> queue의 데이터를 가져간다
    @Override
    public String take() {
        lock.lock();
        try{
            if(queue.isEmpty()) {
                log("[take] 큐에 데이터가 없음, 소비자 대기");
                try {
                    consumerCondition.await();  // 소바자 대기공간에서 소비자를 대기 상태로 보관
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            String data = queue.poll();
            log("[take] 소비자 데이터 획득, 생산자 대기 공간에서 생산자를 깨우기 위해 signal() 호출");
            producercondition.signal(); // 생산자 대기공간에서 생산자를 깨움

            return data;
        }finally {
            lock.unlock();
        }
    }


    @Override
    public String toString() {
        return queue.toString();
    }
}
