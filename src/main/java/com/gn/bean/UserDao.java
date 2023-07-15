package com.gn.bean;

import com.gn.anotation.Component;

@Component
public class UserDao {

    public void findUser(String userName) {
        System.out.println("find user by name: " + userName);
    }
}
