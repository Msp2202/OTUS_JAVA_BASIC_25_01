package basic.algorithms;

import java.util.List;

public class ApplicationAlgorithmsTask3 {
    public static void main(String[] args) {

        List<Integer> sortedList = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        BinarySearchTree<Integer> tree = new BinarySearchTree<>(sortedList); // Исправлено

        System.out.println("Поиск элементов:");
        System.out.println("Найти 5: " + tree.find(5));
        System.out.println("Найти 11: " + tree.find(11));

        System.out.println("\nОтсортированный список:");
        System.out.println(tree.getSortedList());
    }
}
