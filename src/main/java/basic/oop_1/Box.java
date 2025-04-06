package basic.oop_1;

/**
 * Попробуйте реализовать класс по его описания: объекты класса Коробка должны иметь размеры и цвет.
 * Коробку можно открывать и закрывать. Коробку можно перекрашивать.
 * Изменить размер коробки после создания нельзя. У коробки должен быть метод, печатающий информацию о ней в консоль.
 * В коробку можно складывать предмет (если в ней нет предмета),
 * или выкидывать его оттуда (только если предмет в ней есть),
 * только при условии, что коробка открыта (предметом читаем просто строку).
 * Выполнение методов должно сопровождаться выводом сообщений в консоль.
 */
public class Box {

    private int height = 4;
    private int width = 2;
    private int length = 7;
    private String color;
    private boolean doorOpen = true;
    private boolean empty = true;

    public Box(String color) {
        this.color = color;
    }

    public void printInfo() {
        String info = String.format("\nЦвет коробки: -  %s, размер коробки: " +
                        "\nвысота - %d, ширина - %d, длинна - %d. " +
                        "\nКоробка - %s, и в данный момент коробка - %s",
                color, height, width, length, getOpenAndClose(), getFullAndEmpty());
        System.out.println(info);
    }

    public String getFullAndEmpty() {
        if(empty) {
            return "Пустая";
        }
        return "Заполнена";
    }

    public String getOpenAndClose() {
        if(doorOpen){
            return "Открыта";
        }
        return "Закрыта";
    }

    public String open() {
        if (!doorOpen) {
            doorOpen = true;
            return "Коробка открыта";
        }
        return "Коробка уже открыта";
    }

    public String close() {
        if (doorOpen) {
            doorOpen = false;
            return "Коробка закрыта";
        }
        return "Коробка уже закрыта";
    }

    public void put() {
        System.out.print("Складываем в коробку. ");
        if(empty && doorOpen) {
            empty = false;
        }
        System.out.printf("Извините!!!! Не получится положить в коробку т.к. она - %s, %s\n", getOpenAndClose(), getFullAndEmpty());
    }

    public void take() {
        System.out.print("Забираем из коробки. ");
        if (doorOpen && !empty) {
            empty = true;
        }
        System.out.printf("Извините!!!! Не получится Забрать из коробке т.к. она - %s, %s\n", getOpenAndClose(), getFullAndEmpty());
    }
    public void paint(String color) {
        String firstColor = getColor();
        setColor(color);
        System.out.printf("\nПерекрасили коробку из цвета - %s в %s цвет", firstColor, color);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
