package com.swjtu.service;

import com.swjtu.mapper.UserMapper;
import com.swjtu.model.User;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User findOneByUsername(String username){
        return userMapper.findOneByUsername(username);
    }

    public boolean insert(String username,String password){

        User one = userMapper.findOneByUsername(username);
        if (one != null){
            return false;
        }
        // 将用户名作为盐值
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userMapper.insert(user);
        return true;
    }

}
