package executor;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class RunnableTask implements Runnable {

    private final String name;
    private int sleepMs =  1000;

    public RunnableTask(String name) {
        this.name = name;
    }

    public RunnableTask(String name, int sleepMs) {
        this.name = name;
        this.sleepMs = sleepMs;
    }

    @Override
    public void run() {
        // 1초가 걸리는 작업
        log(name + " started");
        sleep(sleepMs);
        log(name + " finished");
    }
}
