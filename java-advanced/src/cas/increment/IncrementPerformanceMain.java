package cas.increment;

public class IncrementPerformanceMain {

    public static final long COUNT = 1000000L;

    public static void main(String[] args) {
        test(new BasicInteger(0));
        test(new VolatileInteger(0));
        test(new SyncInteger(0));
        test(new MyAtomicInteger());
    }

    private static void test(IncrementInteger incrementInteger){
        long start = System.currentTimeMillis();

        for (int i = 0; i < COUNT; i++) {
            incrementInteger.increment();
        }

        long end = System.currentTimeMillis();
        System.out.println(incrementInteger.getClass().getSimpleName() + " " + (end - start) + "ms");
    }
}
