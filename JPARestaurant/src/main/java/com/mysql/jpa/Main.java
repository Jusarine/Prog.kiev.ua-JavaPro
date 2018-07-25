package com.mysql.jpa;

public class Main {

    public static void main(String[] args) {

        DAO dao = new DAO();

        dao.addDish("juice", 1, 1, 10);
        dao.addDish("fish", 1, 1.5, 10);
        dao.addDish("cake", 1, 0.4, 10);
        dao.addDish("meat", 1, 1.2, 10);

        System.out.println(dao.selectByName("juice"));
        dao.selectByPrice(0, 10).forEach(System.out::println);
        dao.selectByDiscount(true).forEach(System.out::println);
        dao.selectRandomDishes(3).forEach(System.out::println);
    }
}
