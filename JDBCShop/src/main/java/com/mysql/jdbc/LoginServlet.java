package com.mysql.jdbc;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "Login", urlPatterns = "/login")
public class LoginServlet extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DAO dao = new DAO(new ConnectionFactory().getConnection());
            dao.createTablesIfNotExist();

            fillDB(req, resp, dao);

            boolean userExist = false;
            if (req.getParameter("button").equals("Sign up")){
                userExist = signUp(req, dao);
            }
            else if (req.getParameter("button").equals("Sign in")){
                userExist = signIn(req, dao);
            }

            printResult(req, resp, userExist);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void fillDB(HttpServletRequest req, HttpServletResponse resp, DAO dao) throws IOException {
        if (req.getParameter("button").equals("Fill DB")){
            dao.fillTables();
            resp.sendRedirect("/");
        }
    }

    private boolean signUp(HttpServletRequest req, DAO dao){
        dao.addClient(req.getParameter("name"), req.getParameter("email"),
                req.getParameter("phoneNumber"), req.getParameter("password"));
        return true;
    }

    private boolean signIn(HttpServletRequest req, DAO dao){
        return (dao.getIdByName(req.getParameter("name")) != -1) &&
                req.getParameter("password").equals(dao.getPasswordByName(req.getParameter("name")));
    }

    private void printResult(HttpServletRequest req, HttpServletResponse resp, boolean userExist) throws IOException {
        HttpSession session = req.getSession(true);

        if (!userExist) {
            PrintWriter writer = resp.getWriter();
            writer.println("<br>Incorrect data!" +
                    "<br><a href=\"#\" onclick=\"history.back();\">Back</a>");
        }
        else {
            session.setAttribute("username", req.getParameter("name"));
            resp.sendRedirect("/action.jsp");
        }
    }
}




