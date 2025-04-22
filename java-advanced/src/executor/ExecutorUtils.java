package executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import static util.MyLogger.log;

public class ExecutorUtils {

    public static void printState(ExecutorService executorService){
        if(executorService instanceof ThreadPoolExecutor poolExecutor){
            int poolSize = poolExecutor.getPoolSize();
            int activeCount = poolExecutor.getActiveCount();
            int size = poolExecutor.getQueue().size();
            long completedTaskCount = poolExecutor.getCompletedTaskCount();

            log("[pool = " + poolSize + ", active = " + activeCount + ", queueTasks = " + size + ", completedTaskCount = " + completedTaskCount + "]");
        }
    }
}
