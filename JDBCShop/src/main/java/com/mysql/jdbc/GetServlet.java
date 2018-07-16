package com.mysql.jdbc;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;

@WebServlet(name="Get", urlPatterns = "/get")
public class GetServlet extends HttpServlet {

    private static final Connection connection = new ConnectionFactory().getConnection();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            if (req.getParameter("get_button").equals("items")){
                printResult(req, resp, getItems().toString());
            }
            else if (req.getParameter("get_button").equals("orders")){
                printResult(req, resp, getOrders().toString());
            }
            else if (req.getParameter("get_button").equals("users")){
                printResult(req, resp, getUsers().toString());
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void printResult(HttpServletRequest req, HttpServletResponse resp, String result) throws IOException {
        HttpSession session = req.getSession(false);
        session.getAttribute("username");

        resp.getWriter().println("<b>Welcome, " + session.getAttribute("username") + "</b><br><br>" +
                result +
                "<br><a href=\"#\" onclick=\"history.back();\">Back</a>" +
                "<br><a href=\"/\">Starter page</a>");
    }

    private StringBuilder getItems() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(
                "SELECT item_id, name, description, price FROM items ");

        return Table.getTable(result);
    }

    private StringBuilder getOrders() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(
                "SELECT item_id, client_id, seller_id, amount, delivery, payment " +
                        "FROM items INNER JOIN orders USING (item_id)");

        return Table.getTable(result);
    }

    private StringBuilder getUsers() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(
                "SELECT client_id AS user_id, name, email, phone_number FROM clients");

        return Table.getTable(result);
    }
}
