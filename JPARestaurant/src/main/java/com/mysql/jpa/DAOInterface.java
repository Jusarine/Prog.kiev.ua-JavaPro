package com.mysql.jpa;

import java.util.List;

public interface DAOInterface {

    void addDish(String name, double price, double weight, int discount);

    Dish selectByName(String name);

    List selectByPrice(double from, double to);

    List selectByDiscount(boolean discount);

    List selectRandomDishes(double maxWeight);
}
