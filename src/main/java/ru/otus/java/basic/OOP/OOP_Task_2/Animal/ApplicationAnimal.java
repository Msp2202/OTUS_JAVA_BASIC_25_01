package ru.otus.java.basic.OOP.OOP_Task_2.Animal;

public class ApplicationAnimal {
    public static void main(String[] args) {
        Dog dog = new Dog("Dog", 5, 2, 120);
        Cat cat = new Cat("Cat", 4, 75);
        Horse horse = new Horse("Horse", 14, 2, 500);

        dog.printInfoEndurance();
        cat.printInfoEndurance();
        horse.printInfoEndurance();
        System.out.println();
        System.out.println("Забег  ----------------------");

        dog.run(25);
        cat.run(25);
        horse.run(225);

        System.out.println();
        System.out.println("Состояние после забега ------------");
        dog.printInfoEndurance();
        cat.printInfoEndurance();
        horse.printInfoEndurance();

        System.out.println();
        System.out.println("Заплыв ----------------------");

        dog.swim(5);
        cat.swim(5);
        horse.swim(75);

        System.out.println();
        System.out.println("Состояние после заплыва ------------");
        dog.printInfoEndurance();
        cat.printInfoEndurance();
        horse.printInfoEndurance();

        dog.swim(55);
        cat.run(75);
        dog.printInfoEndurance();
        cat.printInfoEndurance();
        horse.printInfoEndurance();
        System.out.println();
        System.out.println("Завершили упражнения так как все УСТАЛИ");

    }
}
