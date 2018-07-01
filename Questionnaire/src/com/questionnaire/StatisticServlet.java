package com.questionnaire;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;

@WebServlet(name = "StatisticServlet", urlPatterns = "/statistic")
public class StatisticServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder sb = new StringBuilder("<table>");
        for (User user : UserServlet.users) {
            sb.append("<tr>");
            Class userClass = user.getClass();
            Field[] fields = userClass.getDeclaredFields();
            for (Field field : fields) {
                sb.append("<th>").append(field.getName()).append("</th>");
            }
            sb.append("</tr>");
            sb.append("<tr>");
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    sb.append("<td>").append(field.get(user)).append("</td>");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            sb.append("</tr>");
        }
        sb.append("</table>");

        resp.getWriter().println(sb.toString());
        resp.getWriter().println("<br>Click this link to go to the <a href=\"/index.jsp\">starter page</a>");
    }
}
