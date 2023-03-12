package com.bin.service;

import com.bin.dao.UserDao;
import com.bin.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService, UserDetailsPasswordService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.loadUserByUsername(username);
        user.setRoles(userDao.getRolesByUid(user.getId()));
        return user;
    }


    @Override   //默认使用DelegatingPasswordEncoder 的 Bcrypt加密
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        Integer result = userDao.updatePassword(user.getUsername(), newPassword);
        if(result==1){
            ((User)user).setPassword(newPassword);
        }
        return user;
    }
}
