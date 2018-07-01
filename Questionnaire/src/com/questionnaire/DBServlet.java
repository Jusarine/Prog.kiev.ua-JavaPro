package com.questionnaire;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet(name = "DBServlet", urlPatterns = "/db")
public class DBServlet extends HttpServlet{
    private static Connection connection = null; // хранит соединение с базой данных
    private static Statement statement = null; // хранит и выполняет sql запросы
    private static ResultSet result = null; // получает результаты выполнения sql запросов

    private final static String userName = "root";
    private final static String password = "Tania_2018";
    private final static String url = "jdbc:mysql://localhost:3306/userdb" +
            "?verifyServerCertificate=false" +
            "&useSSL=false" +
            "&requireSSL=false" +
            "&useLegacyDatetimeCode=false" +
            "&amp" +
            "&serverTimezone=UTC";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(url, userName, password);
            statement = connection.createStatement();

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO users (firstName, lastName, age, position) VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, req.getParameter("firstName"));
            preparedStatement.setString(2, req.getParameter("lastName"));
            preparedStatement.setString(3, req.getParameter("age"));
            preparedStatement.setString(4, req.getParameter("position"));

            preparedStatement.executeUpdate();

            try {
                this.doGet(req, resp);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException ignored) { }

        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(url, userName, password);
            statement = connection.createStatement();

            result = statement.executeQuery(
                    "SELECT * FROM users");
            StringBuilder sb = new StringBuilder("<table>");

            sb.append("<tr>");
            sb.append("<th>firstName</th>");
            sb.append("<th>lastName</th>");
            sb.append("<th>age</th>");
            sb.append("<th>position</th>");
            sb.append("</tr>");

            while(result.next()){
                sb.append("<tr>");
                sb.append("<td>").append(result.getString("firstName")).append("</td>");
                sb.append("<td>").append(result.getString("lastName")).append("</td>");
                sb.append("<td>").append(result.getString("age")).append("</td>");
                sb.append("<td>").append(result.getString("position")).append("</td>");
                sb.append("</tr>");
            }
            sb.append("</table>");
            resp.getWriter().println(sb.toString());
            resp.getWriter().println("<br>Click this link to go to the <a href=\"/index.jsp\">starter page</a>");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) result.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException ignored) { }

        }
    }
}
