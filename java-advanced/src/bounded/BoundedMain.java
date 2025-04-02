package bounded;

import java.util.ArrayList;
import java.util.List;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class BoundedMain {

    public static void main(String[] args) {
        // 1. BoundedQueue 선택
        BoundedQueue queue = new BoundedQueueV6_1(2);
        
        // 2. 생산자, 소비자 실행 순서 선택, 반드시 하나만 선택
//         productFirst(queue);
        consumerFirst(queue);
    }

    private static void productFirst(BoundedQueue queue) {
        log("== [생산자 먼저 실행] 시작, " + queue.getClass().getSimpleName() + " ==");
        List<Thread> threadList = new ArrayList<>();
        startProducer(queue, threadList);
        printAllState(queue, threadList);
        startConsumer(queue, threadList);
        printAllState(queue, threadList);
        log("== [생산자 먼저 실행] 종료, " + queue.getClass().getSimpleName() + " ==");
    }

    private static void startProducer(BoundedQueue queue, List<Thread> threadList) {
        System.out.println();
        log("생산자 시작");

        for(int i=1; i<=3; i++){
            Thread producer = new Thread((new ProducerTask(queue, "data" + i)), "producer" + i);
            threadList.add(producer);
            producer.start();
            sleep(100);
        }
    }

    private static void printAllState(BoundedQueue queue, List<Thread> threadList) {
        System.out.println();
        log("현재 상태 출력, 큐 데이터: " + queue);
        for (Thread thread : threadList) {
            log(thread.getName() + ": "  + thread.getState());
        }
    }

    private static void startConsumer(BoundedQueue queue, List<Thread> threadList) {
        System.out.println();
        log("소비자 시작");
        for(int i=1; i<=3; i++){
            Thread consumer = new Thread(new ConsumerTask(queue), "consumer" + i);
            threadList.add(consumer);
            consumer.start();
            sleep(100);
        }
    }

    private static void consumerFirst(BoundedQueue queue) {
        log("== [소비자 먼저 실행] 시작, " + queue.getClass().getSimpleName() + " ==");
        List<Thread> threadList = new ArrayList<>();
        startConsumer(queue, threadList);
        printAllState(queue, threadList);
        startProducer(queue, threadList);
        printAllState(queue, threadList);
        log("== [소비자 먼저 실행] 종료, " + queue.getClass().getSimpleName() + " ==");
    }
}
