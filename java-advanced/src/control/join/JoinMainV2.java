package control.join;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class JoinMainV2 {

    public static void main(String[] args) {
        log("Start");
        SumTask task1 = new SumTask(1, 50);
        SumTask task2 = new SumTask(51, 100);

        final Thread thread1 = new Thread(task1, "Task1");
        final Thread thread2 = new Thread(task2, "Task2");

        thread1.start();
        thread2.start();

        // 정확한 타이밍을 맞추어 기다리기 어렵다!
        log("main Thread sleep()");
        sleep(3000);
        log("main Thread wake()");

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
