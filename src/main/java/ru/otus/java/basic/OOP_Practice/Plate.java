package ru.otus.java.basic.OOP_Practice;

public class Plate {
    int maxAmount;
    int foodAmount;

    public Plate(int maxAmount) {
        this.maxAmount = maxAmount;
        this.foodAmount = maxAmount;

    }

    public int addFood(int amountFood) {
        foodAmount += amountFood;
        return foodAmount > maxAmount ? foodAmount = maxAmount : foodAmount;
    }

    public boolean reducedFood(int appetite) {
        if (appetite <= foodAmount) {
            foodAmount -= appetite;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Plate{" +
                "maxAmount=" + maxAmount +
                ", foodAmount=" + foodAmount +
                '}';
    }
}
