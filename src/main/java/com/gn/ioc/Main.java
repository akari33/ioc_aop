package com.gn.ioc;

import com.gn.ioc.bean.UserService;
import com.gn.ioc.util.ApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ApplicationContext();
        UserService userService = (UserService) context.getBean("userService");
        userService.findUser("gn");
    }
}
