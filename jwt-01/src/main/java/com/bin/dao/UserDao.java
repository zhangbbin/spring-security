package com.bin.dao;

import com.bin.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {

    User login(User user);

}
