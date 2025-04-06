package bounded;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static util.MyLogger.log;

public class BoundedQueueV6_2 implements BoundedQueue{

    private BlockingQueue<String> queue;

    public BoundedQueueV6_2(int max) {
        this.queue = new ArrayBlockingQueue<String>(max);
    }

    @Override
    public void put(String data) {
        // 대기 하지 않음
        boolean result = queue.offer(data);
        log("저장 시도 결과 =  " + result);
    }

    @Override
    public String take() {
        // 대기 하지 않음
        return queue.poll();
    }

    @Override
    public String toString(){
        return queue.toString();
    }
}
