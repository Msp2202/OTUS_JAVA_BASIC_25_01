package ru.otus.java.basic.OOP_Practice;

public class Cat {
    String name;
    int appetite;
    boolean satiety = false;

    public Cat(String name, int appetite) {
        this.name = name;
        this.appetite = appetite;
    }

    public boolean toEat(Plate plate) {
        satiety = plate.reducedFood(appetite);
        return satiety;
    }

    @Override
    public String toString() {
        return "Cat{" +
                "name='" + name + '\'' +
                ", amount=" + appetite +
                ", satiety=" + satiety +
                '}';
    }
}
