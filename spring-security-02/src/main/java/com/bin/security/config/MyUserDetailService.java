package com.bin.security.config;

import com.bin.dao.UserDao;
import com.bin.entity.Role;
import com.bin.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Component
public class MyUserDetailService implements UserDetailsService {

    private final UserDao userDao;

    @Autowired
    public MyUserDetailService(UserDao userDao) {
        this.userDao = userDao;
    }


    //dao ===> springboot + mybatis

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.查询用户
        User user = userDao.loadUserByUsername(username);
        if(ObjectUtils.isEmpty(user)) throw new UsernameNotFoundException("用户名不存在");
        //2.查询权限信息
        List<Role> roles = userDao.getRolesByUid(user.getId());
        user.setRoles(roles);
        return user;
    }
}
