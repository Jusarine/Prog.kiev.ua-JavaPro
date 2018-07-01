package com.questionnaire;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet(name="Statistic", urlPatterns = "/statistic")
public class StatisticServlet extends HttpServlet{

    private static Connection connection = null;
    private static Statement statement = null;
    private static ResultSet result = null;

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
            resp.getWriter().println("<br><a href=\"/questionnaire.jsp\">Questionnaire</a>");
            resp.getWriter().println("<br><a href=\"/\">Starter page</a>");

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
