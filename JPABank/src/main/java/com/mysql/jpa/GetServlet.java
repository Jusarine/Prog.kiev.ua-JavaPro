package com.mysql.jpa;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name="Get", urlPatterns = "/get")
public class GetServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        DAO dao = new DAO();
        StringBuilder sb = new StringBuilder();
        switch (req.getParameter("button")) {
            case "accounts":
                for (Object o : dao.getAccounts()) {
                    Account a = (Account) o;
                    sb.append(a).append("<br>");
                }
                break;
            case "transactions":
                for (Object o : dao.getTransactions()) {
                    Transaction t = (Transaction) o;
                    sb.append(t).append("<br>");
                }
                break;
            case "money":
                sb.append("In your accounts ");
                sb.append(dao.getUserMoneyFromAllAccounts("USD")).append(" USD or ");
                sb.append(dao.getUserMoneyFromAllAccounts("EUR")).append(" EUR or ");
                sb.append(dao.getUserMoneyFromAllAccounts("UAH")).append(" UAH.<br>");
                break;
        }
        printResult(req, resp, sb.toString());

    }

    private void printResult(HttpServletRequest req, HttpServletResponse resp, String result) throws IOException {
        HttpSession session = req.getSession(false);
        session.getAttribute("username");

        resp.getWriter().println("<b>Welcome, " + session.getAttribute("username") + "</b><br><br>" +
                result +
                "<br><a href=\"#\" onclick=\"history.back();\">Back</a>" +
                "<br><a href=\"/\">Starter page</a>");
    }
}
