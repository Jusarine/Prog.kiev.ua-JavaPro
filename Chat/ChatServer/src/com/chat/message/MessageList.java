package com.chat.message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MessageList {
	private static final MessageList msgList = new MessageList();

    private final Gson gson;
	private final List<Message> commonMessages = new LinkedList<>();
	private final Map<Pair<String, String>, LinkedList<Message>> privateMessages = new HashMap<>();
	private final Map<String, LinkedList<Message>> chatRoomMessages = new HashMap<>();

	public static MessageList getInstance() {
		return msgList;
	}
  
  	private MessageList() {
		gson = new GsonBuilder().create();
	}

	public synchronized void add(Message m) {
	    commonMessages.add(m);
	}

	public synchronized void addPrivateMessage(Message m) {
        boolean pairExist = false;
        for (Map.Entry<Pair<String, String>, LinkedList<Message>> map : privateMessages.entrySet()) {

            if (map.getKey().getKey().equals(m.getFrom()) && map.getKey().getValue().equals(m.getTo()) ||
                    map.getKey().getKey().equals(m.getTo()) && map.getKey().getValue().equals(m.getFrom())) {

                pairExist = true;
                LinkedList<Message> temp = map.getValue();
                temp.add(m);
                privateMessages.replace(map.getKey(), temp);
                break;
            }
        }
        if (!pairExist) {
            LinkedList<Message> temp = new LinkedList<>();
            temp.add(m);
            privateMessages.put(new Pair<>(m.getFrom(), m.getTo()), temp);
        }
    }

	public synchronized void addToRoom(Message m, boolean newChatRoom){

	    if (newChatRoom) {
            LinkedList<Message> temp = new LinkedList<>();
            temp.add(m);
            chatRoomMessages.put(m.getTo(), temp);
        }
        else {
            for (Map.Entry<String, LinkedList<Message>> map : chatRoomMessages.entrySet()) {
                if (map.getKey().equals(m.getTo())) {

                    LinkedList<Message> temp = map.getValue();
                    temp.add(m);
                    chatRoomMessages.put(m.getTo(), temp);
                    break;
                }
            }
        }
    }

	public synchronized String toJSON(int n) {
		if (n >= commonMessages.size()) return null;
        return gson.toJson(new JsonMessages(commonMessages, n));
	}

    public synchronized String privateMessageToJSON(int n, String userFrom, String userTo) {

        for (Map.Entry<Pair<String, String>, LinkedList<Message>> map : privateMessages.entrySet()) {

            if (map.getKey().getKey().equals(userFrom) && map.getKey().getValue().equals(userTo) ||
                    map.getKey().getKey().equals(userTo) && map.getKey().getValue().equals(userFrom)) {

                if (n >= map.getValue().size()) return null;
                return gson.toJson(new JsonMessages(map.getValue(), n));
            }
        }
        return null;
    }

    public synchronized String getPrivateChatList(String userLogin){
	    List<String> userNames = new LinkedList<>();
        for (Pair<String, String> pair : privateMessages.keySet()) {
            if (pair.getKey().equals(userLogin)) userNames.add(pair.getValue());
            else if (pair.getValue().equals(userLogin)) userNames.add(pair.getKey());
        }
        return gson.toJson(userNames);
    }

    public synchronized String chatRoomMessageToJSON(int n, String chatRoomName) {

        for (Map.Entry<String, LinkedList<Message>> map : chatRoomMessages.entrySet()) {
            if (map.getKey().equals(chatRoomName)){
                if (n >= map.getValue().size()) return null;
                return gson.toJson(new JsonMessages(map.getValue(), n));
            }
        }
        return null;
    }
}
