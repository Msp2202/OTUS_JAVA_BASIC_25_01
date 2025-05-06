package basic.collections.task_1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Employee {
    private String name;
    private int age;

    public Employee(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public static List<String> getListName(List<Employee> employees) {
        if (employees == null) {
            throw new IllegalArgumentException("Список не может быть null");
        }

        List<String> listName = new ArrayList<>();
        for (Employee employee : employees) {
            listName.add(employee.getName());
        }
        return listName;
    }

    public static List<Integer> getListAge(List<Employee> employees, int minAge) {
        if (employees == null) {
            throw new IllegalArgumentException("Список не может быть null");
        }
        List<Integer> listAge = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee != null && employee.getAge() >= minAge) {
                listAge.add(employee.getAge());
            }
        }
        return listAge;
    }

    public static boolean isAverageAgeAbove(List<Employee> employees, double minAverageAge) {
        if (employees == null) {
            throw new IllegalArgumentException("Список сотрудников не может быть null");
        }
        int totalAge = 0;
        int count = 0;

        for (Employee employee : employees) {
            if (employee == null) {
                throw new IllegalArgumentException("Список содержит null-сотрудников");
            }
            totalAge += employee.getAge();
            count++;
        }

        double averageAge = (double) totalAge / count;
        return averageAge > minAverageAge;
    }

    public static Employee findYoungestEmployee(List<Employee> employees) {
        if (employees == null) {
            throw new IllegalArgumentException("Список сотрудников не может быть null");
        }

        Employee youngest = employees.get(0);
        for (Employee employee : employees) {
            if (employee == null) {
                throw new IllegalArgumentException("Список содержит null-сотрудников");
            }
            if (employee.getAge() < youngest.getAge()) {
                youngest = employee;
            }
        }

        return youngest;
    }

    public String getName () {
        return name;
    }

    public int getAge () {
        return age;
    }
}