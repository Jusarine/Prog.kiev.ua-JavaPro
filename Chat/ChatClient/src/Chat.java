

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javax.sql.rowset.serial.SerialRef;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Scanner;

public class Chat {

    private static final Scanner scanner = new Scanner(System.in);
    private static User user;
    private static String anotherUserLogin;
    private static boolean privateChat;
    private static String chatRoomName;
    private static boolean chatRoom;
    private static boolean newChatRoom;

	public static void main(String[] args) {

		try {
            printMenu();

            String line;
            System.out.println("Enter command: ");
            while (!"exit".equalsIgnoreCase(line = scanner.nextLine())){
                switch (line.toLowerCase()){
                    case "sin":
                        sendUser(false);
                        break;
                    case "sup":
                        sendUser(true);
                        break;
                    case "sout":
                        user = null;
                        break;
                    case "gul":
                        getUserList().forEach(System.out::println);
                        break;
                    case "sat":
                        setAvailable(true);
                        break;
                    case "saf":
                        setAvailable(false);
                        break;
                    case "ca":
                        System.out.println(checkAvailable());
                        break;
                    case "chat":
                        chat();
                        break;
                    case "gpcl":
                        if (getPrivateChatList() != null) getPrivateChatList().forEach(System.out::println);
                        break;
                    case "pc":
                        privateChat();
                        break;
                    case "gcrl":
                        getChatRoomList().forEach(System.out::println);
                        break;
                    case "ccr":
                        chatRoom(true);
                        break;
                    case "cr":
                        chatRoom(false);
                        break;
                    case "menu":
                        printMenu();
                    default:
                        System.err.println("Incorrect command!");
                }
                System.out.println("-----------------------------------------------------------------------");
            }

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			scanner.close();
		}
	}

    private static void printMenu(){
        System.out.println("----------------------- Available commands ----------------------------");
        System.out.println("sin  -  sign in");
        System.out.println("sup  -  sign up");
        System.out.println("sout -  sign out");
        System.out.println("sat  -  set your available = true");
        System.out.println("saf  -  set your available = false");
        System.out.println("ca   -  check user's availability");
        System.out.println("gul  -  get users' list");
        System.out.println("chat -  join chat");
        System.out.println("gpcl -  get your private chat list");
        System.out.println("pc   -  join private chat");
        System.out.println("gcrl -  get chat room list");
        System.out.println("ccr  -  create chat room");
        System.out.println("cr   -  join chat room");
        System.out.println("menu -  print available commands");
        System.out.println("exit -  leave chat or exit application");
        System.out.println("-----------------------------------------------------------------------");
    }

    private static void sendUser(boolean newUser) throws IOException {

        System.out.println("Enter your login: ");
        String login = scanner.nextLine();

        if ("".equals(login)) {
            System.err.println("Login can't be empty!");
            return;
        }

        System.out.println("Enter your password: ");
        String password = scanner.nextLine();

        user = new User(login, password, true);
        int resUser = Utils.send(Utils.getURL() + "/addUser?newUser=" + newUser, user.toJSON());
        if (resUser != 200) { // 200 OK
            user = null;
            System.err.println("HTTP error occured: " + resUser);
            if (newUser) System.err.println("User already exist!");
            else System.err.println("User doesn't exist!");
        }
        else {
            if (newUser) System.out.println("Registration successful.");
            System.out.println("You logged as " + user.getLogin() + ".");
        }
    }

    private static void setAvailable(boolean available) throws IOException {
	    if (user != null) {
	        user.setAvailable(available);
	        Utils.send(Utils.getURL() + "/addUser", user.toJSON());
        }
	    else System.err.println("No user detected!");
    }

    private static boolean checkAvailable() throws IOException {
        System.out.println("Enter user login: ");
        String login = scanner.nextLine();

        for (User u : getUserList()) {
            if (u.getLogin().equals(login)){
                return u.isAvailable();
            }
        }
        return false;
    }

    private static int sendMessages(String from, String to, String url) throws IOException {
        System.out.println("Enter your message: ");
        String text;
        while (!(text = scanner.nextLine()).equalsIgnoreCase("exit")) {

            Message m = new Message(from, to, text);
            int res = Utils.send(Utils.getURL() + url, m.toJSON());

            if (newChatRoom) newChatRoom = false;
            if (res != 200) { // 200 OK
                System.err.println("HTTP error occured: " + res);
                return res;
            }
        }
        return 200;
    }

    private static void chat() throws IOException {
	    privateChat = false;
	    chatRoom = false;

        String login = null;
        if (user != null) login = user.getLogin();

        Thread th = new Thread(new GetThread());
        th.setDaemon(true);
        th.start();

        int res = sendMessages(login, null, "/addMessage");
        if (res != 200){
            th.interrupt();
            System.out.println("Receiver doesn't exist!");
        }
        th.interrupt();

    }

    private static void privateChat() throws IOException {
        chatRoom = false;
	    privateChat = true;

        System.out.println("Enter another user login: ");
        anotherUserLogin = scanner.nextLine();

        String login;
        if (user == null) {
            System.err.println("First sign in!");
            return;
        }
        else login = user.getLogin();

        Thread th = new Thread(new GetThread());
        th.setDaemon(true);
        th.start();

        int res = sendMessages(login, anotherUserLogin, "/addMessage");
        if (res != 200) {
            System.err.println("Receiver doesn't exist!");
            th.interrupt();
        }
        th.interrupt();

    }

    private static void chatRoom(boolean newChatRoom) throws IOException {
	    privateChat = false;
        chatRoom = true;

        System.out.println("Enter chat room name: ");
        chatRoomName = scanner.nextLine();

        if ("".equals(chatRoomName)) {
            System.err.println("Chat room name can't be emty!");
            return;
        }

        String login = null;
        if (user != null) login = user.getLogin();

        Thread th = new Thread(new GetThread());
        th.setDaemon(true);
        th.start();

        int res = sendMessages(login, chatRoomName,
                "/addChatRoom?newChatRoom=" + newChatRoom + "&chatRoomName=" + chatRoomName);
        if (res != 200) {
            if (newChatRoom) System.err.println("Chat room already exist!");
            else System.err.println("Chat room doesn't exist!");
            th.interrupt();
        }

        th.interrupt();
    }

    private static LinkedList<User> getUserList() throws IOException {
        URL url = new URL(Utils.getURL() + "/getUserList");
        return readJson(url, new TypeToken<LinkedList<User>>() {});
    }

    private static LinkedList<String> getPrivateChatList() throws IOException {
	    if (user == null) {
	        System.err.println("First sign in!");
	        return null;
        }
        URL url = new URL(Utils.getURL() + "/getPrivateChatList?userLogin=" + user.getLogin());
        return readJson(url, new TypeToken<LinkedList<String>>() {});
    }

    private static LinkedList<String> getChatRoomList() throws IOException {
        URL url = new URL(Utils.getURL() + "/getChatRoomList");
        return readJson(url, new TypeToken<LinkedList<String>>() {});
    }

    private static <T> T readJson(URL url, TypeToken<T> typeToken) throws IOException {
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        try (InputStream is = http.getInputStream()) {
            byte[] buf = Utils.requestBodyToArray(is);
            String strBuf = new String(buf, StandardCharsets.UTF_8);

            Gson gson = new GsonBuilder().create();
            return gson.fromJson(strBuf, typeToken.getType());
        }
    }

    public static User getUser() {
        return user;
    }

    public static String getAnotherUserLogin() {
        return anotherUserLogin;
    }

    public static boolean isPrivateChat() {
        return privateChat;
    }

    public static boolean isChatRoom() {
        return chatRoom;
    }

    public static String getChatRoomName() {
        return chatRoomName;
    }

    public static boolean isNewChatRoom() {
        return newChatRoom;
    }
}
