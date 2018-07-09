import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class GetThread implements Runnable {
    private final Gson gson;
    private int n;

    public GetThread() {
        gson = new GsonBuilder().create();
    }

    @Override
    public void run() {
        try {
            while ( ! Thread.interrupted()) {
                URL url;
                if (Chat.isPrivateChat()){
                    url = new URL(Utils.getURL() + "/getMessage?from=" + n + "&privateChat=" + Chat.isPrivateChat() +
                            "&userFrom=" + Chat.getUser().getLogin() + "&userTo=" + Chat.getAnotherUserLogin());
                }
                else if (Chat.isChatRoom()){
                    url = new URL(Utils.getURL() + "/getMessage?from=" + n + "&newChatRoom=" + Chat.isNewChatRoom() +
                            "&chatRoomName=" + Chat.getChatRoomName());
                }
                else url = new URL(Utils.getURL() + "/getMessage?from=" + n);

                HttpURLConnection http = (HttpURLConnection) url.openConnection();

                try (InputStream is = http.getInputStream()) {
                    byte[] buf = Utils.requestBodyToArray(is);
                    String strBuf = new String(buf, StandardCharsets.UTF_8);

                    JsonMessages list = gson.fromJson(strBuf, JsonMessages.class);
                    if (list != null) {
                        for (Message m : list.getList()) {
                            System.out.println(m);
                            n++;
                        }
                    }
                }
                Thread.sleep(500);
            }
        } catch (Exception ignored) { }
    }
}
