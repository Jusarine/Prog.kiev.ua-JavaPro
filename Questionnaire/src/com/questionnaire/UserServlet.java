package com.questionnaire;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet(name = "UserServlet", urlPatterns = "/user")
public class UserServlet extends HttpServlet {
    public static List<User> users = Collections.synchronizedList(new LinkedList<>());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String age = req.getParameter("age");
        String position = req.getParameter("position");

        User user = new User.Builder()
                .setFirstName(firstName)
                .setLastName(lastName)
                .setAge(age)
                .setPosition(position)
                .build();

        users.add(user);

        resp.sendRedirect("/statistic");
    }
}
