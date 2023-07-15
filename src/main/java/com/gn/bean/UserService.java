package com.gn.bean;

import com.gn.anotation.Autowired;
import com.gn.anotation.Component;

@Component
public class UserService {
    @Autowired
    private UserDao userDao;

    public void findUser(String userName) {
        userDao.findUser(userName);
    }
}
