package collection.list;

import static util.MyLogger.log;

public class SimpleListMainV2 {

    public static void main(String[] args) throws InterruptedException {
//        test(new SyncList());

        BasicList basicList = new BasicList();
        test(new SyncProxyList(basicList));
    }

    public static void test(SimpleList list) throws InterruptedException {
        log(list.getClass().getSimpleName());

        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                list.add("A");
                log("Thread-1: list.add(A)");
            }
        };

        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                list.add("B");
                log("Thread-2: list.add(B)");
            }
        };

        Thread thread1 = new Thread(runnable1, "Tread-1");
        Thread thread2 = new Thread(runnable2, "Thread-2");

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        log(list);
    }
}
