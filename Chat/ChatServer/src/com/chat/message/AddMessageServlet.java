package com.chat.message;

import com.chat.user.User;
import com.chat.user.UserList;
import com.chat.Utils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "AddMessage", urlPatterns = "/addMessage")
public class AddMessageServlet extends HttpServlet {

	private MessageList msgList = MessageList.getInstance();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        byte[] buf = Utils.requestBodyToArray(req);
        String bufStr = new String(buf, StandardCharsets.UTF_8);

        Message msg = Message.fromJSON(bufStr);

        boolean receiverExist = false;
        for (User user : UserList.getInstance().getList()) {
            if (msg.getTo() == null){
                receiverExist = true;
                msgList.add(msg);
                break;
            }
            else if (user.getLogin().equals(msg.getTo())){
                receiverExist = true;
                msgList.addPrivateMessage(msg);
                break;
            }
        }
        if (!receiverExist) resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	}
}
