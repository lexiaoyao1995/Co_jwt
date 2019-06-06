package com.swjtu.mapper;

import com.swjtu.model.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    public User findOneByUsername(@Param("username") String username);

    public void insert(User user);

}
