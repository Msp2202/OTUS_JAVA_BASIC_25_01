package basic.multithreading.lasson21;

import static basic.multithreading.lasson21.ApplicationLesson21.calculateByFormula;

public class ArrayFiller implements Runnable {
    private final double[] array;
    private final int start;
    private final int and;

    public ArrayFiller(double[] array, int start, int and) {
        this.array = array;
        this.start = start;
        this.and = and;
    }

    @Override
    public void run() {
        for (int i = start; i < and; i++) {
            array[i] = calculateByFormula(i);
        }
    }
}
