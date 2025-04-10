package cas.increment;

public class BasicInteger implements IncrementInteger{

    private int value;

    public BasicInteger(int value) {
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
