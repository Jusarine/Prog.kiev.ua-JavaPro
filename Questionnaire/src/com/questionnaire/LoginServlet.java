package com.questionnaire;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;

@WebServlet(name = "Login", urlPatterns = "/login")
public class LoginServlet extends HttpServlet{
    private static Connection connection = null;
    private static Statement statement = null;
    private static PreparedStatement preparedStatement = null;
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, userName, password);

            if (req.getParameter("button").equals("Sign up")){
                HttpSession session = req.getSession(true);
                session.setAttribute("firstName", req.getParameter("firstName"));
                session.setAttribute("lastName", req.getParameter("lastName"));

                preparedStatement = connection.prepareStatement(
                        "INSERT INTO users (firstName, lastName, password) VALUES (?, ?, ?)");
                preparedStatement.setString(1, req.getParameter("firstName"));
                preparedStatement.setString(2, req.getParameter("lastName"));
                preparedStatement.setString(3, req.getParameter("password"));
                preparedStatement.executeUpdate();

                statement = connection.createStatement();
                result = statement.executeQuery("SELECT * FROM users ORDER BY id DESC LIMIT 1");
                while (result.next()){
                    session.setAttribute("id", result.getString("id"));
                    break;
                }
            }
            else if (req.getParameter("button").equals("Sign in")){
                statement = connection.createStatement();

                result = statement.executeQuery(
                        "SELECT * FROM users");

                while(result.next()){
                    if (req.getParameter("firstName").equals(result.getString("firstName")) &&
                            req.getParameter("password").equals(result.getString("password"))){

                        HttpSession session = req.getSession(true);
                        session.setAttribute("firstName", req.getParameter("firstName"));
                        session.setAttribute("lastName", result.getString("lastName"));
                        session.setAttribute("id", result.getString("id"));
                        break;
                    }
                }
            }
            else if (req.getParameter("button").equals("Send anonymous questionnaire")){
                statement = connection.createStatement();
                statement.executeUpdate("INSERT INTO users (firstName, lastName) VALUES ('Unknown', 'Unknown')");

                HttpSession session = req.getSession(true);
                session.setAttribute("firstName", "Unknown");
                session.setAttribute("lastName", "Unknown");

                result = statement.executeQuery("SELECT * FROM users ORDER BY id DESC LIMIT 1");
                while (result.next()){
                    session.setAttribute("id", result.getString("id"));
                    break;
                }
            }

            resp.sendRedirect("questionnaire.jsp");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException ignored) { }

        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, userName, password);

            HttpSession session = req.getSession(false);

            preparedStatement = connection.prepareStatement(
                    "UPDATE users set age=?, position=? WHERE id=?");
            preparedStatement.setString(1, req.getParameter("age"));
            preparedStatement.setString(2, req.getParameter("position"));
            preparedStatement.setString(3, (String) session.getAttribute("id"));
            preparedStatement.executeUpdate();

            resp.sendRedirect("/statistic");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException ignored) { }

        }
    }
}
