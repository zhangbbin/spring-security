package com.bin.security.config;

import com.bin.exception.KaptchaNotMatchException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 自定义前后端分离的 Filter
 */
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    public static final String FORM_KAPTCHA_KEY = "kaptcha";

    private String kaptchaParameter = FORM_KAPTCHA_KEY;

    public String getKaptchaParameter() {
        return kaptchaParameter;
    }

    public void setKaptchaParameter(String kaptchaParameter) {
        this.kaptchaParameter = kaptchaParameter;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("=================================");
        //1.判断是否是post请求方式
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        //2.判断是否是json格式请求类型
        if(request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)){
            //3.从json数据类型中获取用户名和密码进行认证
            try {
                //获取请求数据
                Map<String,String> userInfo = new ObjectMapper().readValue(request.getInputStream(), Map.class);
                String kaptcha = userInfo.get(getKaptchaParameter());//用来获取数据中的验证码
                String username = userInfo.get(getUsernameParameter());//用来接收用户名
                String password = userInfo.get(getPasswordParameter());//用来接收密码
                String rememberValue = userInfo.get(AbstractRememberMeServices.DEFAULT_PARAMETER);
                if(!ObjectUtils.isEmpty(rememberValue)) {
                    request.setAttribute(AbstractRememberMeServices.DEFAULT_PARAMETER,rememberValue);
                }
                System.out.println("用户名:" + username + "密码:" + password + "是否记住我" + rememberValue);
                //获取session中的验证码
                String sessionVerifyCode = (String) request.getSession().getAttribute("kaptcha");
                if(!ObjectUtils.isEmpty(kaptcha) && !ObjectUtils.isEmpty(sessionVerifyCode)
                        && kaptcha.equalsIgnoreCase(sessionVerifyCode)){
                    //获取用户名和密码
                    UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
                    setDetails(request,authRequest);
                    return this.getAuthenticationManager().authenticate(authRequest);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        throw new KaptchaNotMatchException("验证码不匹配！");
    }
}
