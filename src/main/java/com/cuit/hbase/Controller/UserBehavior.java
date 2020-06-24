package com.cuit.hbase.Controller;

import com.cuit.hbase.Service.userBehavior;
import com.cuit.hbase.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class UserBehavior {
    @Autowired
    userBehavior BehaviorService;


    @PostMapping("/user/concernedto")
    public Response userConcerned(String v1, String v2) {
        BehaviorService.concernedTo(v1, v2);
        return new Response(200, "success", "关注成功");
    }

    @PostMapping("/user/cancelconcernedto")
    public Response userCancelConcerned(String v1, String v2) {
        BehaviorService.cancelConcernedTo(v1, v2);
        return new Response(200, "success", "取关成功");
    }

    @PostMapping("/user/getMyConcerned")
    public Response getMyConcerned(String v) {
        return new Response(200, "success", BehaviorService.getConcerned(v));
    }

    @PostMapping("/user/getMyFans")
    public Response getMyFans(String v) {
        return new Response(200, "success", BehaviorService.getMyFans(v));
    }

    @PostMapping("/user/isConcerned")
    public Response isConcerned(String v1, String v2) {
        return new Response(200, "success", BehaviorService.isConcerned(v1, v2));
    }

    @PostMapping("/user/getNotMyFans")
    public Response isConcerned(String v) {
        return new Response(200, "success", BehaviorService.getNotConcerned(v));
    }
}
