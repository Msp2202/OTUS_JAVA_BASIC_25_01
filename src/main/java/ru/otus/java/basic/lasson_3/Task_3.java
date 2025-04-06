package ru.otus.java.basic.lasson_3;

public class Task_3 {
    public static void main(String[] args) {

        int[][] args_1 = {
                {22, 5, 8, -12, 4, -2},
                {14, -7, 4, -5},
                {1, -6, 8, -18}
        };
        System.out.println("Сумма положительных чисел = " + sumOfPositiveElements(args_1));

        printSquare(4);

        printZeroDiagonal();

        System.out.println("Реализовать метод findMax(int[][] array) = " + findMax(args_1));

        System.out.println("Сумма элементов второй строки двумерного массива sumElementsSecondRow = " + sumElementsSecondRow(args_1));
    }

    /**
     * Реализовать метод sumOfPositiveElements(..),
     * принимающий в качестве аргумента целочисленный двумерный массив,
     * метод должен посчитать и вернуть сумму всех элементов массива, которые больше 0;
     */
    public static int sumOfPositiveElements(int[][] elements) {
        int sumOfPositiveElements = 0;
        for (int i = 0; i < elements.length; i++) {
            for (int j = 0; j < elements[i].length; j++) {
                if (elements[i][j] > 0) {
                    sumOfPositiveElements += elements[i][j];
                }
            }
        }
        return sumOfPositiveElements;
    }

    /**
     * Реализовать метод, который принимает в качестве аргумента int size
     * и печатает в консоль квадрат из символов * со сторонами соответствующей длины;
     */
    public static void printSquare(int numbar) {

        int[][] square = new int[numbar][numbar];
        for (int i = 0; i < square.length; i++) {
            for (int j = 0; j < square[i].length; j++) {
                System.out.print("*" + "  ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Реализовать метод, принимающий в качестве аргумента двумерный целочисленный массив,
     * и зануляющий его диагональные элементы (можете выбрать любую из диагоналей, или занулить обе);
     */
    public static void printZeroDiagonal() {
        int[][] matrix = {
                {1, 2, 3, 8},
                {4, 5, 6, 4},
                {7, 8, 9, 5},
                {3, 6, 3, 5}
        };

        zeroDiagonal(matrix);
        for (int[] row : matrix) {
            for (int element : row) {
                System.out.print(element + "  ");
            }
            System.out.println();
        }

    }

    public static void zeroDiagonal(int[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < n; i++) {
            matrix[i][i] = 0;
        }
    }

    /**
     * Реализовать метод findMax(int[][] array) который должен найти и вернуть максимальный элемент массива;
     */
    public static int findMax(int[][] array) {
        int maxMeaning = 0;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (array[i][j] > maxMeaning) {
                    maxMeaning = array[i][j];
                }
            }
        }
        return maxMeaning;
    }

    /**
     * Реализуйте метод, который считает сумму элементов второй строки двумерного массива, если второй строки не существует,
     * то в качестве результата необходимо вернуть -1
     */
    public static int sumElementsSecondRow(int[][] args) {
        int sumElementsSecondRow = 0;

        for (int i = 0; i < args.length; i++) {
            if (i == 1) {
                for (int j = 0; j < args[1].length; j++) {
                    sumElementsSecondRow += args[i][j];
                }
            }
        }
        return args.length > 1 ? sumElementsSecondRow : -1;
    }

}
