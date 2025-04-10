package cas.increment;

import java.util.ArrayList;
import java.util.List;

import static util.ThreadUtils.sleep;

public class IncrementThreadMain {

    public static final int THREAD_COUNT = 1000;

    public static void main(String[] args) throws InterruptedException {
        test(new BasicInteger(0));
        test(new VolatileInteger(0)); // 단지 CPU의 캐시 메모리와 메인 메모리의 동기화 문제만 해결 -> 근본적인 해결방법은 임계영역에는 한번에 하나의 쓰레드만
        test(new SyncInteger(0));
        test(new MyAtomicInteger());
    }

    private static void test(IncrementInteger incrementInteger) throws InterruptedException {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                sleep(10);
                incrementInteger.increment();
            }
        };

        List<Thread> threadList = new ArrayList<>();
        for(int i=0; i<THREAD_COUNT; i++){
            Thread thread = new Thread(runnable, "thread" + i);
            threadList.add(thread);
            thread.start();
        }

        for (Thread thread : threadList) {
            thread. join();
        }

        int result = incrementInteger.get();
        System.out.println(incrementInteger.getClass().getSimpleName() + ": " + result);
    }
}
