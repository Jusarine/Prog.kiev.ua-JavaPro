package com.spring.mvc.dao;

import com.spring.mvc.model.Contact;
import com.spring.mvc.model.Group;

import java.util.List;

public interface ContactDAO {
    void add(Contact contact);
    void delete(long[] ids);
    List<Contact> list(Group group, int start, int count);
    List<Contact> list(String pattern);
    long count();
}
