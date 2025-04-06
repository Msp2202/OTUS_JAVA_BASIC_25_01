package basic.lesson_2;

import java.util.Arrays;

public class Lasson_2 {
    public static void main(String[] args) {
        receiverNumberAndPrint(3, "Vasy");
        integerArrayAndSumm(1, 5, 9, 4, 2, 12, 8, 2);
        int[] arrays = new int[5];
        receiverNumberAndIntegerArray(4,arrays);
        int[] referenceArray = {2, 8, 9, 4, 2, 1, 8, 2};
        receiverNumberAndReferenceArray(referenceArray, 4);
        int[] array = {2, 8, 9, 4, 9, 1, 8, 8};
        maxSum(array);
    }

    public static void receiverNumberAndPrint(int numbar, String string) {
        for (int i = 0; i < numbar; i++) {
            System.out.println("1. Результат работ метода receiverNumberAndPrint => " + string);
        }
    }

    public static void integerArrayAndSumm(int... args) {
        int summMoreFive = 0;
        for (int i = 0; i < args.length; i++) {
            if (args[i] > 5) {
                summMoreFive += args[i];
            }
        }
        System.out.println("2. Результат метода integerArrayAndSumm => " + summMoreFive);
    }

    public static void receiverNumberAndIntegerArray(int number, int[] array) {

        for (int i = 0; i < array.length; i++) {
            array[i] = number;
        }
        System.out.println("3. Результат метода receiverNumberAndIntegerArray =>   " + Arrays.toString(array));
    }

    public static void receiverNumberAndReferenceArray(int[] args, int numberValue) {

        for (int i = 0; i < args.length; i++) {
            args[i] += numberValue;
        }
        System.out.println("4. Результат метода receiverNumberAndReferenceArray => " + Arrays.toString(args));
    }

    public static void maxSum(int[] args) {

        int sumValue1 = 0;
        int sumValue2 = 0;
        for (int i = 0; i < args.length / 2; i++) {
            sumValue1 += args[i];
        }
        for (int i = args.length / 2; i < args.length; i++) {
            sumValue2 += args[i];
        }
        if(sumValue1 > sumValue2) {
            System.out.println("5. Сумма чисел больше у первой половины => " + sumValue1);
        } else {
            System.out.println("5. Сумма чисел больше у второй половины => " + sumValue2);
        }
    }


}
