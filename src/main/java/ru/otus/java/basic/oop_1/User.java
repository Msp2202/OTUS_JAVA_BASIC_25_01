package ru.otus.java.basic.oop_1;

/**
 * Создайте класс Пользователь (User) с полями: фамилия, имя, отчество, год рождения, email;
 * Реализуйте у класса конструктор, позволяющий заполнять эти поля при создании объекта;
 * В классе Пользователь реализуйте метод, выводящий в консоль информацию о пользователе в виде:
 * ФИО: фамилия имя отчество
 * Год рождения: год рождения
 * e-mail: email
 */
public class User {

    private String lastName;
    private String name;
    private String patronymic;
    private int age;
    private String email;

    public User(String lastName, String name, String patronymic, int age, String email) {
        this.lastName = lastName;
        this.name = name;
        this.patronymic = patronymic;
        this.age = age;
        this.email = email;
    }

    public void printInfo() {
        System.out.println("ФИО: " + lastName + " " + name + " " + patronymic);
        System.out.println("Год рождения: " + age);
        System.out.println("e-mail: " + email);
        System.out.println("-----------------------");
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }
}
