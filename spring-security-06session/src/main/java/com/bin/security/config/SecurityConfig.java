package com.bin.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //自定义内存数据源
    @Bean
    public UserDetailsService userDetailsService(){
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        inMemoryUserDetailsManager.createUser(User.withUsername("root").password("{noop}123").roles("role").build());
        return inMemoryUserDetailsManager;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    //自定义认证规则
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .logout()
                .and()
                .csrf().disable()
                .sessionManagement()//开启会话管理
                .maximumSessions(1)//允许会话最大并发只能一个客户端
                //.expiredUrl("/login");//当用户被挤下线之后的跳转，传统web开发
                .expiredSessionStrategy(event -> {//前后端分离架构
                    HttpServletResponse response = event.getResponse();
                    Map<String,Object> result = new HashMap<>();
                    result.put("status",500);
                    result.put("msg","当前会话已经失效，请重新登录");
                    String s = new ObjectMapper().writeValueAsString(result);
                    response.setContentType("application/json;charset=utf-8");
                    response.getWriter().println(s);
                    response.flushBuffer();
                })
                .maxSessionsPreventsLogin(true);//禁止当前用户登录
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher(){
        return new HttpSessionEventPublisher();
    }
}
