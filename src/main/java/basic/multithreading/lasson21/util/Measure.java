package basic.multithreading.lasson21.util;

public class Measure {
    private static long time;

    public static void stamp() {
        time = System.currentTimeMillis();
    }

    public static void print(String message) {
        Long result = System.currentTimeMillis() - time;
        System.out.println(message + result + "msg");
    }
}
