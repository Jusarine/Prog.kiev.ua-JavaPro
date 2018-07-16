package com.mysql.jdbc;

import java.sql.*;

public class DAO implements DAOInterface{
    private final Connection conn;

    public DAO(Connection conn){
        this.conn = conn;
    }

    @Override
    public void createTablesIfNotExist(){
        createTableCustomers();
        createTableItems();
        createTableOrders();
    }

    private void createTableCustomers(){
        try (Statement st = conn.createStatement()) {

            st.execute("CREATE TABLE IF NOT EXISTS clients (" +
                    "client_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(50) UNIQUE NOT NULL," +
                    "email VARCHAR(50)," +
                    "phone_number VARCHAR(20) NOT NULL," +
                    "password VARCHAR(20))");


        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void createTableOrders(){
        try (Statement st = conn.createStatement()) {

            st.execute("CREATE TABLE IF NOT EXISTS orders (" +
                    "order_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "item_id INT NOT NULL," +
                    "client_id INT NOT NULL," +
                    "amount INT NOT NULL," +
                    "delivery VARCHAR(50) NOT NULL," +
                    "payment VARCHAR(50) NOT NULL," +
                    "FOREIGN KEY (item_id) REFERENCES items (item_id)," +
                    "FOREIGN KEY (client_id) REFERENCES clients (client_id))");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void createTableItems(){
        try (Statement st = conn.createStatement()) {

            st.execute("CREATE TABLE IF NOT EXISTS items (" +
                    "item_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "seller_id INT NOT NULL," +
                    "name VARCHAR(50) NOT NULL," +
                    "description VARCHAR(100)," +
                    "price DOUBLE NOT NULL," +
                    "FOREIGN KEY (seller_id) REFERENCES clients (client_id))");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dropTablesIfExist(){
        try (Statement st = conn.createStatement()) {

            st.execute("DROP TABLE IF EXISTS orders");
            st.execute("DROP TABLE IF EXISTS items");
            st.execute("DROP TABLE IF EXISTS clients");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void fillTables(){
        dropTablesIfExist();
        createTablesIfNotExist();

        addClients();
        addItems();
        addOrders();
    }

    private void addClients(){
        addClient("John", "john5@gmail.com", "+380507424683", "1111");
        addClient("Mark", "mark15@i.ua", "+380978327912", "mark");
        addClient("Emily", "emily36@gmail.com", "+380963578314", "1234");
        addClient("James", "james72@ukr.net", "+380508826437", "james");
    }

    private void addItems(){
        addItem(getIdByName("John"), "flower", "white", 1);
        addItem(getIdByName("Mark"), "T-shirt", "blue", 10.4);
        addItem(getIdByName("Emily"), "vase", "with flowers", 5.8);
        addItem(getIdByName("James"), "cup", "polka dot", 6.2);
    }

    private void addOrders(){
        addOrder(2, getIdByName("John"), 1, Delivery.MailDelivery, Payment.CashOnDelivery);
        addOrder(1, getIdByName("Mark"), 3, Delivery.Pickup, Payment.CreditCardPayment);
        addOrder(3, getIdByName("James"), 1, Delivery.CourierDelivery, Payment.CashlessPayment);
    }

    @Override
    public void addClient(String name, String email, String phoneNumber, String password){
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO clients (name, email, phone_number, password) " +
                        "VALUES (?, ?, ?, ?)"
        )){
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phoneNumber);
            ps.setString(4, password);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public void addItem(int seller_id, String name, String description, double price){
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO items (seller_id, name, description, price) " +
                        "VALUES (?, ?, ?, ?)"
        )){
            ps.setInt(1, seller_id);
            ps.setString(2, name);
            ps.setString(3, description);
            ps.setDouble(4, price);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public void addOrder(int item_id, int client_id, int amount, Delivery delivery, Payment payment){
        try (
                PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO orders (item_id, client_id, amount, delivery, payment) VALUES (?, ?, ?, ?, ?)")
        ){
            ps.setInt(1, item_id);
            ps.setInt(2, client_id);
            ps.setInt(3, amount);
            ps.setString(4, delivery.toString());
            ps.setString(5, payment.toString());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public int getIdByName(String name){
        try (Statement st = conn.createStatement()){
            ResultSet result = st.executeQuery("SELECT client_id, name FROM clients");

            while (result.next()){
                if (result.getString("name").equals(name))
                    return result.getInt("client_id");
            }
            return -1;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public String getPasswordByName(String name){
        try (Statement st = conn.createStatement()){
            ResultSet result = st.executeQuery("SELECT name, password FROM clients");

            while (result.next()){
                if (result.getString("name").equals(name))
                    return result.getString("password");
            }
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}
