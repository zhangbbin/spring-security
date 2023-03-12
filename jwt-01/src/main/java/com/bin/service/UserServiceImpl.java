package com.bin.service;

import com.bin.dao.UserDao;
import com.bin.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User login(User user) {
        //根据接收的和密码查询数据库
        User userDB = userDao.login(user);

        if(userDB!=null){
            return userDB;
        }
        throw new RuntimeException("登录失败");
    }
}
