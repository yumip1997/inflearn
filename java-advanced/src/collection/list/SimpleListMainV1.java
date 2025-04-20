package collection.list;

import java.util.ArrayList;
import java.util.List;

public class SimpleListMainV1 {

    public static void main(String[] args) throws InterruptedException {
        List<Integer> list = new ArrayList<>();

        Runnable adder = () -> {
            for (int i = 0; i < 1000; i++) {
                list.add(i);
            }
        };

        Thread t1 = new Thread(adder);
        Thread t2 = new Thread(adder);

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        // 기대: 2000, 실제: 1800~1999 사이
        System.out.println("Expected size: 2000, Actual size: " + list.size());
    }
}
