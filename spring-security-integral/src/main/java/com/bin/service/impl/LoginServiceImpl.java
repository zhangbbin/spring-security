package com.bin.service.impl;

import com.bin.domain.LoginUser;
import com.bin.domain.ResponseResult;
import com.bin.domain.User;
import com.bin.service.LoginService;
import com.bin.utils.JwtUtil;
import com.bin.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {

    private final AuthenticationManager authenticationManager;

    private final RedisCache redisCache;

    @Autowired
    public LoginServiceImpl(AuthenticationManager authenticationManager, RedisCache redisCache) {
        this.authenticationManager = authenticationManager;
        this.redisCache = redisCache;
    }

    @Override
    public ResponseResult login(User user) {
        //进行认证用户
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        //如果认证未通过，给出相应提示
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("登录失败");
        }

        //如果认证通过了，使用userid生成一个jwt
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userid = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userid);
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);

        //把完整的用户信息存入redis,userid作为key
        redisCache.setCacheObject("login:" + userid, loginUser);

        return new ResponseResult(200, "登陆成功", map);
    }

    @Override
    public ResponseResult logout() {
        //获取SecurityContextHolder

        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();

        //删除redis中的值
        redisCache.deleteObject("login:" + userid);
        return new ResponseResult(200, "注销成功！");
    }
}
