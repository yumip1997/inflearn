package cas.increment;

public class SyncInteger implements IncrementInteger{

    private int value;

    public SyncInteger(int value) {
        this.value = value;
    }

    @Override
    public synchronized void increment() {
        value++;
    }

    @Override
    public int get() {
        return value;
    }

    @Override
    public String toString() {
        return "BasicInteger{" +
                "value=" + value +
                '}';
    }

}
