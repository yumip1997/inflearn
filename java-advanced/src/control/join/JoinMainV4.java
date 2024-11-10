package control.join;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class JoinMainV4 {

    public static void main(String[] args) throws InterruptedException {
        log("Start");
        SumTask task1 = new SumTask(1, 50);
        final Thread thread1 = new Thread(task1, "Task1");

        thread1.start();
        // 쓰레드가 종료될 때까지 main 쓰레드는 대기한다. -> join
        log("join(1000) - main thread is waiting until thread1 and thread2 terminate");
        thread1.join(1000); // terminated 상태가 될때까지 대기

        log("main wake");
        log("task1.result = " + task1.result);
        log("End");
    }

    static class SumTask implements Runnable {

        int startValue;
        int endValue;
        int result = 0;

        public SumTask(int startValue, int endValue){
            this.startValue = startValue;
            this.endValue = endValue;
        }

        @Override
        public void run() {
            log("Task start");
            sleep(2000);

            int sum = 0;
            for(int i =startValue; i <= endValue; i++){
                sum += i;
            }
            result = sum;
            log("Task end result = " + result);
        }

    }
}
