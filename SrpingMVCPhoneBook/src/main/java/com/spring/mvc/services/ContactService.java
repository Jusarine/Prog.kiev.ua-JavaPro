package com.spring.mvc.services;

import com.spring.mvc.model.Contact;
import com.spring.mvc.model.Group;

import java.util.List;

public interface ContactService {
    void addContact(Contact contact);
    void addGroup(Group group);
    void deleteContact(long[] ids);
    void deleteGroup(Group group);
    List<Group> listGroups();
    List<Contact> listContacts(Group group, int start, int count);
    List<Contact> listContacts(Group group);
    long count();
    Group findGroup(long id);
    List<Contact> searchContacts(String pattern);
}
