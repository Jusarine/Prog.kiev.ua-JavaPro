package com.chat.chatRoom;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.LinkedList;
import java.util.List;

public class ChatRoomList {
	private static final ChatRoomList chatRoomList = new ChatRoomList();

    private final Gson gson;
	private final List<String> list = new LinkedList<>();

	public static ChatRoomList getInstance() {
		return chatRoomList;
	}

  	private ChatRoomList() {
		gson = new GsonBuilder().create();
	}

	public synchronized List<String> getList(){
	    return list;
    }
	
	public synchronized void add(String chatRoomName) {
		list.add(chatRoomName);
	}
	
	public synchronized String toJSON() {
		return gson.toJson(list);
	}
}
