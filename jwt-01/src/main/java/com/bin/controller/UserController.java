package com.bin.controller;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bin.entity.User;
import com.bin.service.UserService;
import com.bin.util.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/login")
    public Map<String,Object> login(User user){
        log.info("用户名:[{}]",user.getName());
        log.info("密码:[{}]",user.getPassword());
        Map<String,Object> map = new HashMap<>();
        try{
            User userDB = userService.login(user);
            Map<String,String> payload = new HashMap<>();
            payload.put("id",userDB.getId());
            payload.put("name",userDB.getName());
            //生成JWT令牌
            String token = JWTUtils.getToken(payload);
            map.put("status",true);
            map.put("msg","认证成功！");
            map.put("token",token);//相应token
        }catch (Exception e){
            map.put("status",false);
            map.put("msg",e.getMessage());
        }
        return map;
    }

    @PostMapping("/user/test")
    public Map<String,Object> test(String token){
        HashMap<String, Object> map = new HashMap<>();
        log.info("当前token为：[{}]",token);
        try {
            DecodedJWT verify = JWTUtils.verify(token);
            map.put("status",true);
            map.put("msg","请求成功！");
            return map;
        } catch (SignatureVerificationException e){
            e.printStackTrace();
            map.put("msg","无效签名");
        }
        map.put("status",false);
        return map;
    }
}
