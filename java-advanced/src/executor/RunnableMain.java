package executor;

import java.util.Random;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class RunnableMain {

    public static void main(String[] args) throws InterruptedException {
        MyRunnable myRunnable = new MyRunnable();
        Thread thread = new Thread(myRunnable, "Thread 1");
        thread.start();
        thread.join();
        int result = myRunnable.value;
        log("result: " + result);
    }

    static class MyRunnable implements Runnable {

        int value = 0;

        @Override
        public void run() {
            log("Runnable 시작");
            sleep(2000);
            value = new Random().nextInt(100);
            log("create value : " + value);
            log("Runnable 완료");
        }
    }
}
