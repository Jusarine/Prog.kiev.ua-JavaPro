package com.questionnaire;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;

@WebServlet(name = "Questionnaire", urlPatterns = "/questionnaire")
public class QuestionnaireServlet extends HttpServlet{
    private static Connection connection = null;
    private static PreparedStatement preparedStatement = null;

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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, userName, password);

            HttpSession session = req.getSession(false);
            addUserAnswersToDB(req, session);

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

    private void addUserAnswersToDB(HttpServletRequest req, HttpSession session) throws SQLException {
        preparedStatement = connection.prepareStatement(
                "UPDATE users set age=?, position=? WHERE id=?");
        preparedStatement.setString(1, req.getParameter("age"));
        preparedStatement.setString(2, req.getParameter("position"));
        preparedStatement.setString(3, (String) session.getAttribute("id"));
        preparedStatement.executeUpdate();
    }
}
