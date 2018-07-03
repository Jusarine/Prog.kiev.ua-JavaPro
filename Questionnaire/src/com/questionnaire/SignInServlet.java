package com.questionnaire;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;

@WebServlet(name = "SignIn", urlPatterns = "/signin")
public class SignInServlet extends HttpServlet{
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

            setSessionAttributes(req);
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

    private void setSessionAttributes(HttpServletRequest req) throws SQLException {
        statement = connection.createStatement();
        result = statement.executeQuery(
                "SELECT * FROM users");

        HttpSession session = null;
        while(result.next()){
            if (req.getParameter("firstName").equals(result.getString("firstName")) &&
                    req.getParameter("password").equals(result.getString("password"))){

                session = req.getSession(true);
                session.setAttribute("firstName", req.getParameter("firstName"));
                session.setAttribute("lastName", result.getString("lastName"));
                session.setAttribute("id", result.getString("id"));
                break;
            }
        }
    }
}



