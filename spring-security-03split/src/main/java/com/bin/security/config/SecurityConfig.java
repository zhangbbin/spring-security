package com.bin.security.config;

import com.bin.service.MyUserDetailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailService myUserDetailService;

    //基于内存的数据源
    /*@Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        inMemoryUserDetailsManager.createUser(User.withUsername("root").password("{noop}123").roles("admin").build());
        return inMemoryUserDetailsManager;
    }*/


    //自定义数据源，属于工厂内部的自定义方法，需要暴露出来
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailService);
    }


    @Bean
    @Override
    //暴露出来，可被其他方法实例化
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //自定义 Filter 交给工厂管理
    @Bean
    public LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter();
        loginFilter.setFilterProcessesUrl("/doLogin");//指定认证url
        loginFilter.setUsernameParameter("uname");//指定接受json用户名key
        loginFilter.setPasswordParameter("passwd");//指定接收json密码key
        loginFilter.setKaptchaParameter("kaptcha");//指定接收json验证码key
        //指定认证管理器
        loginFilter.setAuthenticationManager(authenticationManagerBean());//设置认证数据源
        loginFilter.setRememberMeServices(rememberMeServices());//设置成功时使用自定义rememberMeService
        loginFilter.setAuthenticationSuccessHandler((request, response, authentication) -> {
            Map<String, Object> result = new HashMap<>();
            result.put("msg", "登录成功");
            result.put("用户信息", authentication.getPrincipal());
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpStatus.OK.value());
            String s = new ObjectMapper().writeValueAsString(result);
            response.getWriter().println(s);
        });//认证成功处理
        loginFilter.setAuthenticationFailureHandler((request, response, exception) -> {
            Map<String, Object> result = new HashMap<>();
            result.put("msg", "登录失败:" + exception.getMessage());
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            String s = new ObjectMapper().writeValueAsString(result);
            response.getWriter().println(s);
        });//认证失败处理
        return loginFilter;
    }

    //自定义权限规则
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .mvcMatchers("/vc.jpg").permitAll()
                .anyRequest().authenticated()//所有请求必须认证
                .and()
                .formLogin()
                .and()
                .rememberMe()
                .rememberMeServices(rememberMeServices())//设置自动登录使用哪个rememberMeService
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.getWriter().println("尚未认证，请认证之后再去处理!");
                })
                .and()
                .logout()
                //.logoutSuccessUrl("/logout")
                .logoutRequestMatcher(new OrRequestMatcher(
                        new AntPathRequestMatcher("/logout", HttpMethod.DELETE.name()),
                        new AntPathRequestMatcher("/logout", HttpMethod.GET.name())
                ))
                .logoutSuccessHandler((request, response, authentication) -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("msg", "注销成功");
                    result.put("用户信息", authentication.getPrincipal());
                    response.setContentType("application/json;charset=UTF-8");
                    response.setStatus(HttpStatus.OK.value());
                    String s = new ObjectMapper().writeValueAsString(result);
                    response.getWriter().println(s);
                })
                .and()
                .csrf().disable();

        // at:用自定义的Filter代替链中的默认的Filter
        // before:放在过滤器链中的哪个过滤器之前
        // after:放在过滤器链中的哪个过滤器之后
        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public RememberMeServices rememberMeServices() {
        return new MyPersistentTokenBasedRememberMeServices(UUID.randomUUID().toString(), userDetailsService(), new InMemoryTokenRepositoryImpl());
    }
}
