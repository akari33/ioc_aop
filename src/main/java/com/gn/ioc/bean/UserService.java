package com.gn.ioc.bean;

import com.gn.ioc.anotation.Autowired;
import com.gn.ioc.anotation.Component;

@Component
public class UserService {
    @Autowired
    private UserDao userDao;

    public void findUser(String userName) {
        userDao.findUser(userName);
    }
}
