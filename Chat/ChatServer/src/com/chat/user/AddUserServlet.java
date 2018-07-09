package com.chat.user;

import com.chat.Utils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "AddUser", urlPatterns = "/addUser")
public class AddUserServlet extends HttpServlet {

    private UserList userList = UserList.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        byte[] buf = Utils.requestBodyToArray(req);
        String bufStr = new String(buf, StandardCharsets.UTF_8);

        User user = User.fromJSON(bufStr);

        addUser(req, resp, user);
    }

    private void addUser(HttpServletRequest req, HttpServletResponse resp, User user){

        boolean newUser = Boolean.parseBoolean(req.getParameter("newUser"));

        if (newUser){
            if (getUserFromList(user) == null) {
                userList.add(user);
            }
            else resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        else {
            if (getUserFromList(user) != null) {
                userList.update(user);
            }
            else resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private User getUserFromList(User user){
        for (User u : userList.getList()) {
            if (u.getLogin().equals(user.getLogin()) && u.getPassword().equals(user.getPassword())) {
                return u;
            }
        }
        return null;
    }
}
