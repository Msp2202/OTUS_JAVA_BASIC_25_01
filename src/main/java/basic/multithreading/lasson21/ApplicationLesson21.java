package basic.multithreading.lasson21;

import basic.multithreading.lasson21.util.Measure;

import java.util.ArrayList;
import java.util.List;

public class ApplicationLasson21 {

    private static double SIZE = 100_000_000;

    public static void main(String[] args) {
        List<Integer> arrList = createArrayList();
        Measure.stamp();

    }

    private static List<Integer> createArrayList() {
        List<Integer> arr = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            arr.add(i);
        }
        return arr;
    }
}
