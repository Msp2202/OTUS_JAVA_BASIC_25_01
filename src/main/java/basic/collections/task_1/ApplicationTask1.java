package basic.collections.task_1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import static basic.collections.task_1.Employee.findYoungestEmployee;

public class ApplicationTask1 {

    private static final int MIN = 3;
    private static final int MAX = 9;
    private static final int MIN_AGE = 20;
    private static final double MIN_AVERAGE_AGE = 28.8;

    public static void main(String[] args) {

        System.out.println("Результат первого метода = " + range(MIN, MAX));

        List<Integer> array_2 = new ArrayList<>(Arrays.asList(1, 12, 15, 2, 48, 4, 1));
        System.out.println("Результат второго метода = " + summ(array_2));

        List<Integer> array_3 = new ArrayList<>(Arrays.asList(1, 12, 15, 2, 48, 4, 1, 1, 2, 2));
        System.out.println("Результат теретьего метода = " + fillListWithNumber(MAX, array_3));

        List<Integer> array_4 = new ArrayList<>(Arrays.asList(1, 12, 15, 2, 48, 4, 1, 1, 2, 2));
        System.out.println("Результат четвертого метода = " + addNumberToAllElements(MAX, array_4));

        List<Employee> employees = new ArrayList<>(Arrays.asList(
                new Employee("Иван" , 25),
                new Employee("Петр" , 35),
                new Employee("Сергей" , 18),
                new Employee("Константин" , 33),
                new Employee("Василиса" , 45)
        ));

       List<String> names = Employee.getListName(employees);
        System.out.println("Список имен сотрудников " + names);

       List<Integer> ages = Employee.getListAge(employees, MIN_AGE);
        System.out.printf("Список сотрудников не старше %d: %s%n ", MIN_AGE, ages);

        boolean isAbove = Employee.isAverageAgeAbove(employees, MIN_AVERAGE_AGE);
        System.out.printf("Средний возраст сотрудников %s порога %.1f лет\n",
                isAbove ? "превышает" : "не превышает",
                MIN_AVERAGE_AGE);

        try {
            Employee youngest = findYoungestEmployee(employees);
            System.out.printf("Самый молодой сотрудник: %s, возраст: %d лет\n",
                    youngest.getName(),
                    youngest.getAge());
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }

    public static List<Integer> range(int minInt, int maxInt) {

        int counter = minInt;
        List<Integer> arr = new ArrayList<>();
        for (int i = minInt; i < maxInt; i++) {
            arr.add(counter);
            counter++;
        }
        return arr;
    }

    public static int summ(List<Integer> arr) {
        int summ = 0;
        for (Integer i : arr) {
            if (i > 5) {
                summ += i;
            }
        }
        return summ;
    }

    public static List<Integer> fillListWithNumber(int number, List<Integer> list) {
        if (list == null) {
            throw new IllegalArgumentException("Список не может быть null");
        }
        ListIterator<Integer> iterator = list.listIterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.set(number);
        }
        return list;
    }

    public static List<Integer> addNumberToAllElements(int number, List<Integer> list){
        if(list == null) {
            throw new IllegalArgumentException("Список не может быть null");
        }
        ListIterator<Integer> iterator = list.listIterator();
        while (iterator.hasNext()){
            iterator.set(iterator.next() + number);
        }
        return list;
    }
}
