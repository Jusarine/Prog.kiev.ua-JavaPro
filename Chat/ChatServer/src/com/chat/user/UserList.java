package com.chat.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.LinkedList;
import java.util.List;

public class UserList {
	private static final UserList userList = new UserList();

    private final Gson gson;
	private final List<User> list = new LinkedList<>();
	
	public static UserList getInstance() {
		return userList;
	}
  
  	private UserList() {
		gson = new GsonBuilder().create();
	}

	public synchronized List<User> getList(){
	    return list;
    }
	
	public synchronized void add(User user) {
		list.add(user);
	}

	public synchronized void update(User user) {
        User temp = null;
        for (User u : list) {
            if (u.getLogin().equals(user.getLogin()) && u.getPassword().equals(user.getPassword())) {
                temp = u;
                break;
            }
        }
        list.remove(temp);
        list.add(user);
    }
	
	public synchronized String toJSON() {
		return gson.toJson(list);
	}
}
