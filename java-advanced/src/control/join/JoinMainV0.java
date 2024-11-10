package control.join;

import util.ThreadUtils;

import static util.MyLogger.log;

public class JoinMainV0 {

    public static void main(String[] args) {
        log("START");

        Thread thread1 = new Thread(new Job(), "Thread-1");
        Thread thread2 = new Thread(new Job(), "Thread-2");

        thread1.start();
        thread2.start();

        log("END");
    }


    static class Job implements Runnable{
        @Override
        public void run() {
            log("JOB START");
            ThreadUtils.sleep(2000);
            log("JOB END");
        }
    }
}
