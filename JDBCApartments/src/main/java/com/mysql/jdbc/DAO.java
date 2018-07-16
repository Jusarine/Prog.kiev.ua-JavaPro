package com.mysql.jdbc;

import java.math.BigDecimal;
import java.sql.*;

public class DAO implements DAOInterface{
    private final Connection conn;

    public DAO(Connection conn){
        this.conn = conn;
    }

    @Override
    public void initDB(){

        deleteTablesIfExist();

        createTableAddress();
        createTableApartments();

        insertValuesToAddress();
        insertValuesToApartments();
    }

    private void deleteTablesIfExist(){
        try (Statement st = conn.createStatement()) {

            st.execute("DROP TABLE IF EXISTS apartments");
            st.execute("DROP TABLE IF EXISTS address");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void createTableAddress(){
        try (Statement st = conn.createStatement()) {

            st.execute("CREATE TABLE address (" +
                    "address_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "town VARCHAR(30) NOT NULL," +
                    "district VARCHAR(30) NOT NULL," +
                    "street VARCHAR(30) NOT NULL," +
                    "house VARCHAR(11) NOT NULL," +
                    "apartment INT NOT NULL)");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void createTableApartments(){
        try (Statement st = conn.createStatement()) {

            st.execute("CREATE TABLE IF NOT EXISTS apartments (" +
                    "apartment_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "address_id INT NOT NULL," +
                    "area DECIMAL(10, 3) NOT NULL," +
                    "room_count INT NOT NULL," +
                    "price INT NOT NULL," +
                    "FOREIGN KEY (address_id) REFERENCES address (address_id))");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void insertValuesToAddress(){
        addEntryToAddress("Kiev", "Darnitskiy", "M. Dragomanov", "38", 18);
        addEntryToAddress("Lviv", "Shevchenkivsky", "Ocheretyan", "12", 23);
        addEntryToAddress("Kiev", "Pechersky", "H. Barbusse", "37/1", 31);
        addEntryToAddress("Kharkov", "Dzerzhinsky", "Micheskaya", "32", 16);
        addEntryToAddress("Kiev","Obolonskiy","Northern", "2/58", 45);
    }

    private void insertValuesToApartments(){
        addEntryToApartments(1, BigDecimal.valueOf(79.6),2, 1711400);
        addEntryToApartments(2, BigDecimal.valueOf(54.41), 2, 636597);
        addEntryToApartments(3, BigDecimal.valueOf(78.9),2, 3567858);
        addEntryToApartments(4, BigDecimal.valueOf(19), 1, 388400);
        addEntryToApartments(5, BigDecimal.valueOf(97), 3, 2520000);
    }

    private void addEntryToAddress(String town, String district, String street, String house, int apartment){
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO address (town, district, street, house, apartment) " +
                        "VALUES (?, ?, ?, ?, ?)"
        )){
            ps.setString(1, town);
            ps.setString(2, district);
            ps.setString(3, street);
            ps.setString(4, house);
            ps.setInt(5, apartment);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private void addEntryToApartments(int address_id, BigDecimal area, int room_count, int price){
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO apartments (address_id, area, room_count, price) " +
                        "VALUES (?, ?, ?, ?)"
        )){
            ps.setInt(1, address_id);
            ps.setBigDecimal(2, area);
            ps.setInt(3, room_count);
            ps.setInt(4, price);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public void selectByPrice(int from, int to){
        selectByParameter("price", from, to);
    }

    @Override
    public void selectByRoomCount(int from, int to){
        selectByParameter("room_count", from, to);
    }

    @Override
    public void selectByArea(BigDecimal from, BigDecimal to){
        selectByParameter("area", from, to);
    }

    @Override
    public void selectByTown(String town){
        selectByParameter("town", town);
    }

    @Override
    public void selectByDistrict(String district){
        selectByParameter("district", district);
    }

    private <T> void selectByParameter(String parameter, T from, T to){
        System.out.println("Selected by " + parameter + " between " + from + " and " + to);
        try (Statement st = conn.createStatement()){

            st.executeQuery("SELECT area, room_count, price, town, district, street, house, apartment FROM apartments " +
                    "INNER JOIN address USING(address_id) WHERE " + parameter + " BETWEEN " + from + " AND " + to);

            printTable(st);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private void selectByParameter(String parameter, String value){
        System.out.println("Selected by " + parameter + " = " + value);
        try (Statement st = conn.createStatement()){

            st.executeQuery("SELECT area, room_count, price, town, district, street, house, apartment FROM apartments " +
                    "INNER JOIN address USING(address_id) WHERE " + parameter + " = '" + value + "'");

            printTable(st);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private void printTable(Statement st) throws SQLException {
        ResultSet result = st.getResultSet();
        ResultSetMetaData md = result.getMetaData();

        printColumnNames(md);
        while (result.next()) {
            System.out.print(" | ");
            for (int i = 1; i <= md.getColumnCount(); i++) {
                System.out.printf("%14s", result.getObject(md.getColumnName(i)));
                System.out.print(" | ");
            }
            System.out.println();
        }
        System.out.println("\n");
    }

    private void printColumnNames(ResultSetMetaData md) throws SQLException {
        System.out.print(" | ");
        for (int i = 1; i <= md.getColumnCount(); i++){
            System.out.printf("%14s", md.getColumnName(i));
            System.out.print(" | ");
        }
        System.out.println();
    }
}
