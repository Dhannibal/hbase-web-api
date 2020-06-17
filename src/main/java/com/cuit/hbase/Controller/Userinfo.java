package com.cuit.hbase.Controller;

import com.cuit.hbase.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class Userinfo {
    @GetMapping("/user/getuserByrk")
    public User getuserByrk(HttpSession session) {
        User user = new User();
        user.setName("张三");
        user.setEmail("825857667@qq.com");
        user.setRowkey("!!!!");
        System.out.println("---！");
        return user;
    }
}
