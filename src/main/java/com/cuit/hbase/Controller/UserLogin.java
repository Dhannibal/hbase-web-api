package com.cuit.hbase.Controller;

import com.cuit.hbase.Service.LoginService;
import com.cuit.hbase.model.User;
import com.cuit.hbase.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;



@CrossOrigin
@RestController
public class UserLogin {

    @Autowired
    LoginService loginService;


    @PostMapping("/user/login")
    public Response Login(User user) {
        return loginService.login(user);
    }

    @PostMapping("/user/register")
    public Response Register(User user) {
        return loginService.register(user);
    }
}
