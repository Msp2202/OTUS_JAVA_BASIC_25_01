package ru.otus.java.basic.OOP.OOP_Task_2;


/**
 * класса Animal
 * У каждого животного есть имя, скорость бега и плавания (м/с), и выносливость (измеряется в условных единицах)
 * Затраты выносливости:
 * Все животные на 1 метр бега тратят 1 ед выносливости,
 * Собаки на 1 метр плавания - 2 ед.
 * Лошади на 1 метр плавания тратят 4 единицы
 * Кот плавать не умеет.
 * Собаки на 1 метр плавания - 2 ед.
 * Лошади на 1 метр плавания тратят 4 единицы
 * Кот плавать не умеет.
 * Реализуйте методы run(int distance) и swim(int distance), которые должны возвращать время,
 * затраченное на указанное действие, и “понижать выносливость” животного.
 * Если выносливости не хватает, то возвращаем время -1 и указываем что у животного появилось состояние усталости.
 * При выполнении действий пишем сообщения в консоль.
 * Добавляем метод info(), который выводит в консоль состояние животного.
 */
public class Animal {
    private String name;
    private Integer speedRun;
    private Integer speedSwim;
    private Integer endurance;
    private Integer expensesSwim;

    public Animal(String name, Integer speedRun, Integer speedSwim, Integer endurance, Integer expensesRun, Integer expensesSwim) {
        this.name = name;
        this.speedRun = speedRun;
        this.speedSwim = speedSwim;
        this.endurance = endurance;
        this.expensesSwim = expensesSwim;
    }

    public void printInfoEndurance() {
        System.out.printf("\nЖивотное по имене %s находится в состояние -: %s", name, endurance);
    }

    public void swim(int distance) {
        float time = distance / speedSwim;
        endurance -= expensesSwim;
        if(endurance < 0) {
            System.out.printf("\nЖивотное по имене %s появилась усталость.", name);
            System.out.println("время -1");
        }
        System.out.printf("\nЖивотное с именем %s. Проплыли %d за время = %d.", name, distance, time);
    }

    public void run(int distance) {
        float time = distance / speedRun;
        endurance -= distance;
        if(endurance < 0) {
            printInfoEndurance();
            System.out.println("время -1");
        }
        System.out.printf("\nЖивотное с именем %s. Пробежали %d за время = %d.", name, distance, time);
    }

}
