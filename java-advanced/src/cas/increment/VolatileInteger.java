package cas.increment;

public class VolatileInteger implements IncrementInteger{

    volatile private int value;

    public VolatileInteger(int value) {
        this.value = value;
    }

    @Override
    public void increment() {
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
