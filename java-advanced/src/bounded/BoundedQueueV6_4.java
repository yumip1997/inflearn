package bounded;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BoundedQueueV6_4 implements BoundedQueue{

    private BlockingQueue<String> queue;

    public BoundedQueueV6_4(int max) {
        this.queue = new ArrayBlockingQueue<String>(max);
    }

    @Override
    public void put(String data) {
        queue.add(data);    // 큐가 가득찼다면 해당 쓰레드에서 java.lang.IllegalStateException 발생
    }

    @Override
    public String take() {
        return queue.remove(); // 큐가 비었다면 해당 쓰레드에서 java.util.NoSuchElementException 발생
    }

    @Override
    public String toString(){
        return queue.toString();
    }
}
