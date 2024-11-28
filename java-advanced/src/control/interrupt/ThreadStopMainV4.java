package control.interrupt;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class ThreadStopMainV4 {

    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread thread = new Thread(task, "work");
        thread.start();

        sleep(100);
        log("stop task thread.interrupt()");
        thread.interrupt(); // 쓰레드를 개운다!
        log("thread interrupted state1 = " + thread.isInterrupted());
    }

    static class MyTask implements Runnable {
        @Override
        public void run() {
            while (!Thread.interrupted()){ //인터럽트 상태 변경 o
                log("tasking");
            }
            log("thread interrupted state2 = " + Thread.currentThread().isInterrupted());

            try{
                log("resource clean");
                Thread.sleep(1000);
                log("resource end");
            }catch (InterruptedException e){
                log("state = " + Thread.currentThread().getState());
            }

        }
    }
}
