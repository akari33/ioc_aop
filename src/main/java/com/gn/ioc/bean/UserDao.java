package com.gn.ioc.bean;

import com.gn.ioc.anotation.Component;

@Component
public class UserDao {

    public void findUser(String userName) {
        System.out.println("find user by name: " + userName);
    }
}
