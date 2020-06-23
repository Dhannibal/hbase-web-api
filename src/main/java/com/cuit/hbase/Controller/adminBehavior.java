package com.cuit.hbase.Controller;


import com.cuit.hbase.Service.userBehavior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class adminBehavior {
    @Autowired
    userBehavior UserBehaviorService;

    @RequestMapping("/admin/createInfo")
    public String createInfo() {
        UserBehaviorService.createInfo();
        return "success";
    }
}
