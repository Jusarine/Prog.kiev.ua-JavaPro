package com.mysql.jpa;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

public class DAO implements DAOInterface {

    private static final String NAME = "JPATest";
    private static final EntityManagerFactory emFactory = Persistence.createEntityManagerFactory(NAME);
    private static final EntityManager em = emFactory.createEntityManager();

    @Override
    public void addDish(String name, double price, double weight, int discount){
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(new Dish(name, price, weight, discount));
        transaction.commit();
    }

    @Override
    public Dish selectByName(String name){
        System.out.println("\nSelected by name = " + name);
        Query query = em.createQuery("select d from Dish d where d.name = :name");
        query.setParameter("name", name);
        return (Dish) query.getSingleResult();
    }

    @Override
    public List<Dish> selectByPrice(double from, double to){
        System.out.println("\nSelected by price between " + from + " and " + to);
        Query query = em.createQuery("select d from Dish d where d.price between :from and :to");
        query.setParameter("from", from);
        query.setParameter("to", to);
        return query.getResultList();
    }

    @Override
    public List<Dish> selectByDiscount(boolean discount){
        System.out.println("\nSelected by discount = " + discount);
        Query query;
        if (discount) query = em.createQuery("select d from Dish d where d.discount > 0");
        else query = em.createQuery("select d from Dish d where d.discount = 0");
        return query.getResultList();
    }

    @Override
    public List<Dish> selectRandomDishes(double maxWeight){
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
