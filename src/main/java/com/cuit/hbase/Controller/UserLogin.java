package com.cuit.hbase.Controller;

import com.cuit.hbase.dao.HbaseConnector;
import com.cuit.hbase.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@CrossOrigin
@RestController
public class UserLogin {

    @Autowired
    HbaseConnector hbaseConnector;

    @RequestMapping("/test")
    public String test() {
        hbaseConnector.createTable("testtable", "basic");
        return "test";
    }

    @PostMapping("/user/login")
    public String Login(User user) {
        System.out.println(user.getEmail());
        System.out.println(user.getPwd());

        return "main";
    }

    @PostMapping("/user/register")
    public String Register(User user) {
        System.out.println(user.getName());
        System.out.println(user.getPwd());
        System.out.println(user.getEmail());

        return "index";
    }
}
