package ru.otus.java.basic.OOP_Practice;

public class ApplicationTest {
    public static void main(String[] args) {

        int amount;
        Plate plate = new Plate(100);
        Cat[] cat = {
                new Cat("Барсик", 15),
                new Cat("Мурзик", 35),
                new Cat("Васка", 15),
                new Cat("Базилий", 45),
        };

        print(cat, plate);
        System.out.println(plate);

        System.out.println("---------------");

        amount = 10;
        plate.addFood(amount);
        System.out.println(plate);

        System.out.println("---------------");

        cat[3].toEat(plate);
        System.out.println(cat[3]);

        System.out.println("---------------");

        System.out.println(plate);
        amount = 150;
        plate.addFood(amount);
        System.out.println(plate);
    }

    public static void print(Cat[] cat, Plate plate) {
        for (int i = 0; i < cat.length; i++) {
            cat[i].toEat(plate);
            System.out.println(cat[i]);
        }
    }
}
