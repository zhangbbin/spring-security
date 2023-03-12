package com.bin.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Map;

public class JWTUtils {

    private static final String SING = "!Q@W#E$R%%#wwew";

    /**
     * 生成token header.payload.sing
     */
    public static String getToken(Map<String,String> map){
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE,7);  //默认7天过期

        //创建jwt builder
        JWTCreator.Builder builder = JWT.create();

        //payload
        map.forEach((k,v)->{
            builder.withClaim(k,v);
        });

        String token = null;
        try {
            token = builder.withExpiresAt(instance.getTime())//指定令牌过期时间
                    .sign(Algorithm.HMAC256(SING));//sign
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    /**
     * 验证token 合法性
     */
    public static DecodedJWT verify(String token){
        try {
            DecodedJWT verify = JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
            return verify;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取token信息
     */
//    public static DecodedJWT getTokenInfo(String token){
//        try {
//            DecodedJWT verify = JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
//            return verify;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

}
