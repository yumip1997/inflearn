package bounded;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static util.MyLogger.log;

public class BoundedQueueV4 implements BoundedQueue {

    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition(); // 쓰레드 대기공간 생성

    private final Queue<String> queue = new ArrayDeque<>();
    private final int maxSize;

    public BoundedQueueV4(int maxSize) {
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
                    condition.await();  // Object.wait()와 유사 -> 지정한 condition에 현재 스레드를 대기 상태로 보관
                    log("[put] 생산자 깨어남");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }


            queue.offer(data);
            log("[put] 생산자 데이터 저장, notify() 호출");
            condition.signal(); // 지정한 condition에서 대기 중인 다른 스레드를 깨움
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
                    condition.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            String data = queue.poll();
            log("[take] 소비자 데이터 획득, notify() 호출");
            condition.signal();

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
