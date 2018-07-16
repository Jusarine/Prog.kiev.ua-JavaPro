package com.mysql.jdbc;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "Add", urlPatterns = "/add")
public class AddServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DAO dao = new DAO(new ConnectionFactory().getConnection());
            dao.createTablesIfNotExist();

            if (req.getParameter("add_button").equals("Add item")){
                addItem(req, dao);
                printResult(resp, "<b>Added successfully!</b><br>");
            }
            else if (req.getParameter("add_button").equals("Make an order")){
                addOrder(req, dao);
                printResult(resp, "<b>Ordered successfully!</b><br>");
            }

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    private void printResult(HttpServletResponse resp, String message) throws IOException {
        resp.getWriter().println(message +
                "<br><a href=\"#\" onclick=\"history.back();\">Back</a>" +
                "<br><a href=\"/\">Starter page</a>");
    }

    private void addItem(HttpServletRequest req, DAO dao){
        HttpSession session = req.getSession(false);
        String username = String.valueOf(session.getAttribute("username"));

        dao.addItem(dao.getIdByName(username),
                req.getParameter("name"),
                req.getParameter("description"),
                Double.parseDouble(req.getParameter("price")));
    }

    private void addOrder(HttpServletRequest req, DAO dao){
        HttpSession session = req.getSession(false);
        String username = String.valueOf(session.getAttribute("username"));

        dao.addOrder(Integer.parseInt(req.getParameter("item_id")),
                dao.getIdByName(username),
                Integer.parseInt(req.getParameter("amount")),
                Delivery.valueOf(req.getParameter("delivery")),
                Payment.valueOf(req.getParameter("payment")));
    }

}
