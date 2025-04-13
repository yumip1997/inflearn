package collection.list;

import java.util.Arrays;

import static util.ThreadUtils.sleep;

public class BasicList implements SimpleList {

    private static final int INITIAL_CAPACITY = 5;

    private Object[] elements;

    private int size = 0;

    public BasicList() {
        elements = new Object[INITIAL_CAPACITY];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(Object o) {
        elements[size] = o;
        sleep(100); // 멀티스레드 문제를 쉽게 확인하는 코드
        size++;
    }

    @Override
    public Object get(int index) {
        return elements[index];
    }

    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOf(elements, size)) + "size= " + size
                + " capacity = " + elements.length;
    }
}
