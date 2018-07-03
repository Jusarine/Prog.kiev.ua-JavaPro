package com.questionnaire;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;

@WebServlet(name = "SignUp", urlPatterns = "/signup")
public class SignUpServlet extends HttpServlet{
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
                createUser(req);
            }
            else if (req.getParameter("button").equals("Send anonymous questionnaire")){
                createAnonymousUser(req);
            }
            resp.sendRedirect("/questionnaire.jsp");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
                if (result != null) result.close();
            } catch (SQLException ignored) { }
        }
    }

    private void createUser(HttpServletRequest req) throws SQLException {
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String password = req.getParameter("password");

        addUserInfoToDB(firstName, lastName, password);
        setSessionAttributes(req, firstName, lastName);
    }

    private void createAnonymousUser(HttpServletRequest req) throws SQLException {
        addUserInfoToDB("Unknown", "Unknown", "");
        setSessionAttributes(req, "Unknown", "Unknown");
    }

    private void addUserInfoToDB(String firstName, String lastName, String password) throws SQLException {
        preparedStatement = connection.prepareStatement(
                "INSERT INTO users (firstName, lastName, password) VALUES (?, ?, ?)");

        preparedStatement.setString(1, firstName);
        preparedStatement.setString(2, lastName);
        preparedStatement.setString(3, password);
        preparedStatement.executeUpdate();
    }

    private void setSessionAttributes(HttpServletRequest req, String firstName, String lastName) throws SQLException {
        HttpSession session = req.getSession(true);
        session.setAttribute("firstName", firstName);
        session.setAttribute("lastName", lastName);

        statement = connection.createStatement();
        result = statement.executeQuery("SELECT * FROM users ORDER BY id DESC LIMIT 1");
        while (result.next()){
            session.setAttribute("id", result.getString("id"));
            break;
        }
    }
}




