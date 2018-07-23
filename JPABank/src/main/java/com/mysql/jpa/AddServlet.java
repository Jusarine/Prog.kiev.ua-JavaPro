package com.mysql.jpa;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(name = "Add", urlPatterns = "/add")
public class AddServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

        DAO dao = new DAO();

        switch (req.getParameter("button")) {
            case "Add account":
                dao.addAccount(req.getParameter("currency"));
                break;
            case "Replenish account":
                dao.replenish(
                        Long.valueOf(req.getParameter("account_id")),
                        BigDecimal.valueOf(Long.valueOf(req.getParameter("money"))),
                        req.getParameter("currency"));
                break;
            case "Perform transaction":
                dao.performTransaction(
                        Long.valueOf(req.getParameter("sender_id")),
                        Long.valueOf(req.getParameter("receiver_id")),
                        BigDecimal.valueOf(Long.valueOf(req.getParameter("money"))),
                        req.getParameter("currency"));
                break;
            case "Change account currency":
                dao.changeAccountCurrency(
                        Long.valueOf(req.getParameter("account_id")),
                        req.getParameter("currency"));
                break;
        }
        printResult(resp);
    }

    private void printResult(HttpServletResponse resp){
        try {
            resp.getWriter().println("<b>Successfully done!</b><br>" +
                    "<br><a href=\"#\" onclick=\"history.back();\">Back</a>" +
                    "<br><a href=\"/\">Starter page</a>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
