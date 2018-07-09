package com.chat.chatRoom;

import com.chat.message.Message;
import com.chat.message.MessageList;
import com.chat.Utils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "AddChatRoom", urlPatterns = "/addChatRoom")
public class AddChatRoomServlet extends HttpServlet {

    private ChatRoomList chatRoomList = ChatRoomList.getInstance();
    private MessageList msgList = MessageList.getInstance();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        byte[] buf = Utils.requestBodyToArray(req);
        String bufStr = new String(buf, StandardCharsets.UTF_8);
        Message msg = Message.fromJSON(bufStr);

        String chatRoomName = req.getParameter("chatRoomName");
        boolean newChatRoom = Boolean.parseBoolean(req.getParameter("newChatRoom"));

        boolean roomExist = isRoomExist(chatRoomName);

        if ((newChatRoom && roomExist) || (!newChatRoom && !roomExist)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        else {
            if (newChatRoom) {
                chatRoomList.add(chatRoomName);
            }
            msgList.addToRoom(msg, newChatRoom);
        }
	}

	private boolean isRoomExist(String chatRoomName){
        for (String name : chatRoomList.getList()) {
            if (name.equals(chatRoomName)){
                return true;
            }
        }
        return false;
    }
}
