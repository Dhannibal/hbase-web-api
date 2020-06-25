package com.cuit.hbase.Controller;

import com.cuit.hbase.Service.userBehavior;
import com.cuit.hbase.model.User;
import com.cuit.hbase.model.pairUser;
import com.cuit.hbase.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class UserBehavior {
    @Autowired
    userBehavior BehaviorService;


    @PostMapping("/user/concernedto")
    public Response userConcerned(@RequestBody pairUser users) {
        BehaviorService.concernedTo(users.getU1(), users.getU2());
        return new Response(200, "success", "关注成功");
    }

    @PostMapping("/user/cancelconcernedto")
    public Response userCancelConcerned(@RequestBody pairUser users) {
        BehaviorService.cancelConcernedTo(users.getU1(), users.getU2());
        return new Response(200, "success", "取关成功");
    }

    @PostMapping("/user/getMyConcerned")
    public Response getMyConcerned(@RequestBody User v) {
        return new Response(200, "success", BehaviorService.getConcerned(v.getEmail()));
    }

    @PostMapping("/user/getMyFans")
    public Response getMyFans(@RequestBody User v) {
        return new Response(200, "success", BehaviorService.getMyFans(v.getEmail()));
    }

    @PostMapping("/user/isConcerned")
    public Response isConcerned(@RequestBody pairUser users) {
        return new Response(200, "success", BehaviorService.isConcerned(users.getU1(), users.getU2()));
    }

    @PostMapping("/user/getNotMyFans")
    public Response isConcerned(@RequestBody User v) {
        System.out.println(v.getEmail());
        return new Response(200, "success", BehaviorService.getNotConcerned(v.getEmail()));
    }
}
