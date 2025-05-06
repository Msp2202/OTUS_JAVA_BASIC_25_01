package basic.network;


public class Calculator {
    public static double calculate(double num1, String operation, double num2) {
        switch (operation) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                if (num2 == 0) throw new IllegalArgumentException("Деление на ноль невозможно");
                return num1 / num2;
            default:
                throw new IllegalArgumentException("Неизвестная операция: " + operation);
        }
    }
}