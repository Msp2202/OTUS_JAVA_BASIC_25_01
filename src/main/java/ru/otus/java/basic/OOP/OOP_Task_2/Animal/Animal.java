package ru.otus.java.basic.OOP.OOP_Task_2.Animal;

public class Animal {

    protected String name;
    protected int speedRun;
    protected int speedSwim;
    protected int endurance;
    protected int expensesSwim;

    public Animal(String name, int speedRun, int speedSwim, int endurance, int expensesSwim) {
        this.name = name;
        this.speedRun = speedRun;
        this.speedSwim = speedSwim;
        this.endurance = endurance;
        this.expensesSwim = expensesSwim;
    }

    public void printInfoEndurance() {
        System.out.printf("\nЖивотное по имене %s находится в состояние: %s ", name, endurance);
    }

    public void swim(int distance) {
        if (speedSwim == 0) {
            System.out.printf("\n%s не умеет плавать!", name);
            return;
        }

        int time = distance / speedSwim;
        endurance -= (expensesSwim * distance);

        if(endurance < 0) {
            System.out.printf("\nУ животного по имени %s появилась УСТАЛОСТИ.", name);
            System.out.print(" время -1");
        } else {
            System.out.printf("\nЖивотное с именем %s. Проплыли %d за время = %d. ", name, distance, time);
        }
    }

    public void run(int distance) {
        int time = distance/ speedRun;
        endurance -= distance;
        if(endurance < 0) {
            System.out.printf("\nУ животного по имени %s появилась УСТАЛОСТИ.", name);
            System.out.println(" время -1");
        } else {
            System.out.printf("\nЖивотное с именем %s. Пробежали %d за время = %d.", name, distance, time);
        }
    }
}
