package executor;

import java.util.Random;
import java.util.concurrent.*;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class CallableMainV1 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        log("submit() 호출");
        Future<Integer> future = executorService.submit(new MyCallable());
        log("future 즉시 반환, future = " + future);

        log("future.get() [블로킹] 메서드 호출 시작 -> main 스레드 WAITING");
        Integer result = future.get(); // 호출한 쓰레드가 기다린다 -> blocking
        log("future.get() [블로킹] 메서드 호출 완료 -> main 스레드 RUNNING");

        log("result value = " + result);
        log("future 완료, future = " + future);
        executorService.close();
    }

    static class MyCallable implements Callable<Integer> {

        @Override
        public Integer call(){
            log("callable 시작");
            sleep(3000);

            int value = new Random().nextInt(100);
            log("callable 끝");

            return value;
        }
    }
}
