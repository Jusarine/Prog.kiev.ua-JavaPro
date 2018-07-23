package com.mysql.jpa;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

public class Main {

    private static final String NAME = "JPATest";
    private static EntityManagerFactory emFactory;
    private static EntityManager em;

    public static void main(String[] args) {

        emFactory = Persistence.createEntityManagerFactory(NAME);
        em = emFactory.createEntityManager();

        addDish("juice", 1, 1, 10);
        addDish("fish", 1, 1.5, 10);
        addDish("cake", 1, 0.4, 10);
        addDish("meat", 1, 1.2, 10);

        System.out.println(selectByName("juice"));
        selectByPrice(0, 10).forEach(System.out::println);
        selectByDiscount(true).forEach(System.out::println);
        selectRandomDishes(3).forEach(System.out::println);

    }

    public static void addDish(String name, double price, double weight, int discount){
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(new Dish(name, price, weight, discount));
        transaction.commit();
    }

    public static Object selectByName(String name){
        System.out.println("\nSelected by name = " + name);
        Query query = em.createQuery("select d from Dish d where d.name = :name");
        query.setParameter("name", name);
        return query.getSingleResult();
    }

    public static List selectByPrice(double from, double to){
        System.out.println("\nSelected by price between " + from + " and " + to);
        Query query = em.createQuery("select d from Dish d where d.price between :from and :to");
        query.setParameter("from", from);
        query.setParameter("to", to);
        return query.getResultList();
    }

    public static List selectByDiscount(boolean discount){
        System.out.println("\nSelected by discount = " + discount);
        Query query;
        if (discount) query = em.createQuery("select d from Dish d where d.discount > 0");
        else query = em.createQuery("select d from Dish d where d.discount = 0");
        return query.getResultList();
    }

    public static List selectRandomDishes(double maxWeight){
        System.out.println("\nSelected set of dishes with weight = " + maxWeight);

        Query query = em.createQuery("SELECT d from Dish d");

        List<Dish> dishes = new LinkedList<>();
        double w = 0;
        for (Object o : query.getResultList()) {
            Dish d = (Dish) o;
            if (w + d.getWeight() > maxWeight) continue;
            w += d.getWeight();
            dishes.add(d);
        }
        return dishes;
    }
}
