package bounded;

import java.util.ArrayDeque;
import java.util.Queue;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class BoundedQueueV3 implements BoundedQueue {

    private final Queue<String> queue = new ArrayDeque<>();
    private final int maxSize;

    public BoundedQueueV3(int maxSize) {
        this.maxSize = maxSize;
    }

    // 생산자 -> queue의 데이터를 넣는다
    // 임계 영역: synchronized 안 붙이면 maxSize를 넘길 수도 있다! => 한 번에 하나의 쓰레드만 접근해야하는 영역
    @Override
    public synchronized void put(String data) {
        while(queue.size() == maxSize) {
            log("[put] 큐가 가득 참, 생산자 잠시 대기" + data);
            try {
                wait(); // wait를 호출한 스레드 RUNNABLE -> WAITING 상태 전환, 락 반남
                log("[put] 생산자 깨어남");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        queue.offer(data);
        log("[put] 생산자 데이터 저장, notify() 호출");
        notify(); // 대기 중인 소비자 스레드 깨우기
    }

    // 소비자 -> queue의 데이터를 가져간다
    @Override
    public synchronized String take() {
        if(queue.isEmpty()) {
            log("[take] 큐에 데이터가 없음, 소비자 대기");
            try {
                wait(); // wait를 호출한 스레드 RUNNABLE -> WAITING 상태 전환, 락 반남
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        String data = queue.poll();
        log("[take] 소비자 데이터 획득, notify() 호출");
        notify(); // 대기 중인 생산자 스레드 깨우기
        return data;
    }


    @Override
    public String toString() {
        return queue.toString();
    }
}
