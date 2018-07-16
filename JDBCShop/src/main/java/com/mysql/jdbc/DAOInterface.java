package com.mysql.jdbc;

public interface DAOInterface {

    void createTablesIfNotExist();

    void dropTablesIfExist();

    void fillTables();

    void addClient(String name, String email, String phoneNumber, String password);

    void addItem(int seller_id, String name, String description, double price);

    void addOrder(int item_id, int client_id, int amount, Delivery delivery, Payment payment);

}
