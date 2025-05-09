package basic.multithreading.util;

public class Measure {
    private static long time;

    public void stamp() {
        time = System.currentTimeMillis();
    }

    public void print() {
        Long result = System.currentTimeMillis() - time;
        System.out.println("Time" + result);
    }
}
