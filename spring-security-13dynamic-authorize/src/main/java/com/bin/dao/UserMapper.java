package com.bin.dao;

import com.bin.entity.Role;
import com.bin.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    //根据用户id获取角色信息
    List<Role> getUserRoleByUid(Integer uid);
    //根据用户名获取用户信息
    User loadUserByUsername(String username);
}