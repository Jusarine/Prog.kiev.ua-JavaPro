package com.mysql.jpa;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "Login", urlPatterns = "/login")
public class LoginServlet extends HttpServlet{

    static{
        DAO.fillDB();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        DAO dao = new DAO();
        boolean userExist = false;
        if (req.getParameter("button").equals("Sign up")){
            userExist = signUp(req, dao);
        }
        else if (req.getParameter("button").equals("Sign in")){
            userExist = signIn(req, dao);
        }
        printResult(req, resp, userExist);
    }


    private boolean signUp(HttpServletRequest req, DAO dao){
        return dao.signUp(req.getParameter("name"), req.getParameter("email"),
                req.getParameter("phoneNumber"), req.getParameter("password"));
    }

    private boolean signIn(HttpServletRequest req, DAO dao){
        return dao.signIn(req.getParameter("name"), req.getParameter("password"));
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




