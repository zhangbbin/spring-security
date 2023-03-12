package com.bin.service;

import com.bin.dao.UserDao;
import com.bin.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

//自定义实现UserDetailService
@Service
public class MyUserDetailService implements UserDetailsService {

    private final UserDao userDao;

    @Autowired
    public MyUserDetailService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userDao.loadUserByUsername(username);
        if(ObjectUtils.isEmpty(user)) throw new RuntimeException("用户名不存在");
        user.setRoles(userDao.getRolesByUid(user.getId()));
        return user;
    }
}
