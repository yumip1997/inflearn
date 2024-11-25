package control.interrupt;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class ThreadStopMainV2 {

    public static void main(String[] args) {
        final MyTask myTask = new MyTask();
        final Thread thread = new Thread(myTask, "work");
        thread.start(); // 3초마다 작업중이랑는 로그를 찍은 thread

        sleep(4000);
        log("stop task thread.interrupt()");
        thread.interrupt(); // 쓰레드를 개운다!
        log("thread interrupted state1 = " + thread.isInterrupted());
    }

    static class MyTask implements Runnable {
        @Override
        public void run() {
            try{
                while (true){
                    log("tasking");
                    Thread.sleep(3000);
                }
            }catch (InterruptedException e){
                log("thread interrupted state2 = " + Thread.currentThread().isInterrupted());
                log("interrupt message = " + e.getMessage());
                log("state = " + Thread.currentThread().getState());
            }
            log("resource clean");
            log("resource end");
        }
    }

}
