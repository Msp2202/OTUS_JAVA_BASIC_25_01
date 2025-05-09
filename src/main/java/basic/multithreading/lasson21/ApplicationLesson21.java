package basic.multithreading.lasson21;

import basic.multithreading.lasson21.util.Measure;

public class ApplicationLesson21 {

    //    private static double SIZE = 10;
    private static int SIZE = 100_000_000;
    private static int THREAD_COUNT = 4;

    public static void main(String[] args) throws InterruptedException {


        Measure.stamp();
        double[] arr1 = createSingleThread();
        Measure.print("Single Thread: ");

        Measure.stamp();
        double[] arr2 = createMultiThread();
        Measure.print("Multi Thread: ");

    }

    private static double[] createMultiThread() throws InterruptedException {
        double[] array = new double[SIZE];
        Thread[] threads = new Thread[THREAD_COUNT];

        for (int i = 0; i < THREAD_COUNT; i++) {
            int start = i * (SIZE / THREAD_COUNT);
            int and = (i == (THREAD_COUNT - 1)) ? SIZE : (i + 1) * (SIZE / THREAD_COUNT);

            threads[i] = new Thread(new ArrayFiller(array, start, and));
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        return array;
    }

    private static double[] createSingleThread() {
        double[] arr = new double[SIZE];
        for (int i = 0; i < SIZE; i++) {
            arr[i] = calculateByFormula(i);
        }
        return arr;
    }

    public static Double calculateByFormula(double num) {
        Double result = 1.14 * Math.cos(num) * Math.sin(num * 0.2) * Math.cos(num / 1.2);
        return result;
    }

}
