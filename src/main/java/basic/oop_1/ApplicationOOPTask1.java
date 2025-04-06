package basic.oop_1;

/**
 * В методе main() Main класса создайте массив из 10 пользователей и заполните его объектами и
 * с помощью цикла выведите информацию только о пользователях старше 40 лет.
 * Попробуйте реализовать класс по его описания: объекты класса Коробка должны иметь размеры и цвет.
 * Коробку можно открывать и закрывать. Коробку можно перекрашивать.
 * Изменить размер коробки после создания нельзя. У коробки должен быть метод, печатающий информацию о ней в консоль.
 * В коробку можно складывать предмет (если в ней нет предмета), или выкидывать его оттуда
 * (только если предмет в ней есть), только при условии что коробка открыта (предметом читаем просто строку).
 * Выполнение методов должно сопровождаться выводом сообщений в консоль.
 */

public class ApplicationOOPTask1 {
    public static void main(String[] args) {
        User[] users = {
                new User("Петров", "Петр", "Петрович", 25, "pert@mail.ru"),
                new User("Иванов", "Петр", "Иванович", 55, "pert@mail.ru"),
                new User("Сидорова", "Елена", "Сергеевна", 15, "pert@mail.ru"),
                new User("Матросов", "Петр", "Ивановис", 48, "prt@mail.ru"),
                new User("Гольцова", "Ирина", "Петровна", 32, "gol@mail.ru"),
                new User("Дроздова", "Наталья", "Иванова", 57, "drozd@mail.ru"),
                new User("Краков", "Сергей", "Дмитриевич", 68, "kr@mail.ru"),
                new User("Иванова", "Мария", "Петрович", 12, "ivan@mail.ru"),
                new User("Яковлев", "Петр", "Александрович", 31, "ypa@mail.ru"),
                new User("Петров", "Александр", "Петрович", 55, "pert@mail.ru")
        };

        System.out.println("Список Users, чей возраст старше 40 лет");
        for (User user : users) {
            if (user.getAge() > 40) {
                user.printInfo();
            }
        }

        Box box = new Box("зеленый");
        box.printInfo();
        box.open();
        box.close();
        box.put();
        box.take();
        box.open();
        box.put();
        box.close();
        box.printInfo();
        System.out.println("-----------");
        box.open();
        box.printInfo();
        box.take();
        box.printInfo();
        box.paint("Красный");
        box.printInfo();

    }
}
