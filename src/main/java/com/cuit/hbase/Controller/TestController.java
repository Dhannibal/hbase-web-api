package com.cuit.hbase.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class TestController {
    @RequestMapping("/index")
    public String index(){
        return "index";
    }
}
