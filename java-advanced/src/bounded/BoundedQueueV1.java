package bounded;

import java.util.ArrayDeque;
import java.util.Queue;

import static util.MyLogger.log;

public class BoundedQueueV1 implements BoundedQueue {

    private final Queue<String> queue = new ArrayDeque<>();
    private final int maxSize;

    public BoundedQueueV1(int maxSize) {
        this.maxSize = maxSize;
    }

    // 생산자 -> queue의 데이터를 넣는다
    // 임계 영역: synchronized 안 붙이면 maxSize를 넘길 수도 있다! => 한 번에 하나의 쓰레드만 접근해야하는 영역
    @Override
    public synchronized void put(String data) {
        if(queue.size() == maxSize) {
            log("[put] 큐가 가득 참, 버림:" + data);
            return;
        }

        queue.offer(data);
    }

    // 소비자 -> queue의 데이터를 가져간다
    @Override
    public synchronized String take() {
        if(queue.isEmpty()) {
            return null;
        }

        return queue.poll();
    }


    @Override
    public String toString() {
        return queue.toString();
    }
}
