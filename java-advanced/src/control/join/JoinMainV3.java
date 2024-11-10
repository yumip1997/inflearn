package control.join;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class JoinMainV3 {

    public static void main(String[] args) throws InterruptedException {
        log("Start");
        SumTask task1 = new SumTask(1, 50);
        SumTask task2 = new SumTask(51, 100);

        final Thread thread1 = new Thread(task1, "Task1");
        final Thread thread2 = new Thread(task2, "Task2");

        thread1.start();
        thread2.start();

        // 쓰레드가 종료될 때까지 main 쓰레드는 대기한다. -> join
        log("join() - main thread is waiting until thread1 and thread2 terminate");
        thread1.join(); // terminated 상태가 될때까지 대기
        thread2.join(); // terminated 상태가 될때까지 대기
        log("main wake");

        log("task1.result = " + task1.result);
        log("task2.result = " + task2.result);

        int sumAll =  task1.result + task2.result;
        log("task1 + task2 = " + sumAll);

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
            // sleep(2000);

//            int sum = 0;
//            for(int i =startValue; i <= endValue; i++){
//                sum += i;
//            }
//            result = sum;

            result = 100;
            
            log("Task end result = " + result);
        }

    }
}
