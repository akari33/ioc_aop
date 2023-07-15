package com.gn;

import com.gn.bean.UserService;
import com.gn.util.ApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ApplicationContext();
        UserService userService = (UserService) context.getBean("userService");
        userService.findUser("gn");
    }
}
