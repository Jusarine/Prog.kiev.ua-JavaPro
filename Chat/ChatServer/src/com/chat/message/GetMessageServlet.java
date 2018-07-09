package com.chat.message;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "GetMessage", urlPatterns = "/getMessage")
public class GetMessageServlet extends HttpServlet {
	
	private MessageList msgList = MessageList.getInstance();

    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String json = getJsonMessage(req, resp);

		if (json != null) {
			OutputStream os = resp.getOutputStream();
            byte[] buf = json.getBytes(StandardCharsets.UTF_8);
			os.write(buf);

			//PrintWriter pw = resp.getWriter();
			//pw.print(json);
		}
	}

	private String getJsonMessage(HttpServletRequest req, HttpServletResponse resp){

        int from = getFrom(req, resp);
        resp.setContentType("application/json");

        String json;
        if (Boolean.parseBoolean(req.getParameter("privateChat"))) {
            json = msgList.privateMessageToJSON(from, req.getParameter("userFrom"), req.getParameter("userTo"));
        }
        else if (req.getParameter("chatRoomName") != null) {
            json = msgList.chatRoomMessageToJSON(from, req.getParameter("chatRoomName"));
        }
        else {
            json = msgList.toJSON(from);
        }
        return json;
    }

	private int getFrom(HttpServletRequest req, HttpServletResponse resp){
        String fromStr = req.getParameter("from");
        int from = 0;
        try {
            from = Integer.parseInt(fromStr);
            if (from < 0) from = 0;
        } catch (Exception ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return from;
    }
}
