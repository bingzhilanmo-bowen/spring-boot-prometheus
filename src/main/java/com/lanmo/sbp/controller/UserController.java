package com.lanmo.sbp.controller;

import com.lanmo.sbp.model.User;
import com.lanmo.sbp.rpc.Rpc;
import com.lanmo.sbp.service.UserService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private Rpc rpc;


    @GetMapping("/get")
    public User getUser(Integer id){
        Metrics.counter("GET_USER", "ID", id+"").increment(1);
        return userService.getUser(id);
    }

    @GetMapping("/list")
    public Object getList(){
        try {
            Metrics.counter("GET_LIST").increment(1);
            return rpc.getList();
        }catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

}
