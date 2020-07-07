package com.lanmo.sbp.service;

import com.lanmo.sbp.dao.UserDAO;
import com.lanmo.sbp.model.User;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    public void addUser(){
        User user = new User();
        user.setName("test");
        user.setAddress("cd");
        user.setEmail("t@qq.com");
        user.setCreateTime(new Date());
        user.setRole(1);
        userDAO.insert(user);
    }

    public User getUser(Integer id){
        return userDAO.selectByPrimaryKey(id);
    }

}
