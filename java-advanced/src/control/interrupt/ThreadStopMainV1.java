package control.interrupt;

import util.ThreadUtils;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class ThreadStopMainV1 {

    public static void main(String[] args) {
        final MyTask myTask = new MyTask();
        final Thread thread = new Thread(myTask, "work");
        thread.start(); // 3초마다 작업중이랑는 로그를 찍은 thread

        sleep(4000);
        log("task stop runFlag=false");
        myTask.runFlag = false; // 즉각적으로 myTask가 while문을 빠져나오지 않는다.

        // -> 어떻게 하면 쓰레드가 sleep하고 있는 상황에서 급하게 꺠울 수 있을까?
    }

    static class MyTask implements Runnable {

        volatile boolean runFlag = true;

        @Override
        public void run() {
            while (runFlag){
                log("tasking");
                sleep(3000);
            }

            log("resource clean");
            log("resource end");
        }
    }
}
