package com.bin.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    private final MyUserDetailService myUserDetailService;

    @Autowired
    public WebSecurityConfigurer(MyUserDetailService myUserDetailService) {
        this.myUserDetailService = myUserDetailService;
    }


//    @Bean
//    public UserDetailsService userDetailsService(){
//        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
//        userDetailsManager.createUser(User.withUsername("aaa").password("{noop}123").roles("admin").build());
//        return userDetailsManager;
//    }

    //springboot对security 默认配置中 在工厂默认创建AuthenticationManager
//    @Autowired
//    public void initialize(AuthenticationManagerBuilder builder) throws Exception {
//        System.out.println("springboot默认配置:" + builder);
//        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
//        userDetailsManager.createUser(User.withUsername("aaa").password("{noop}123").roles("admin").build());
//        builder.userDetailsService(userDetailsManager);
//    }

    //自定义AuthenticationManager,自定义会覆盖默认的构造器,推荐使用，并没有在工厂内暴露
    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        System.out.println("自定义配置AuthenticationManager" + builder);
        builder.userDetailsService(myUserDetailService);
    }

    //用来将自定义的AuthenticationManager在工厂中进行暴露，可以在任何位置注入
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/login.html").permitAll()
                .mvcMatchers("/index").permitAll()//放行资源写在任何前面
                .anyRequest().authenticated()
                .and()
                .formLogin()//表单登录
                .loginPage("/login.html")//用来指定默认的登录页面 注意，一旦自定义登录页面，必须只能登录url
                .loginProcessingUrl("/doLogin")//指定处理登录的请求URL
                .usernameParameter("uname")
                .passwordParameter("passwd")//自定义参数名称
                //.successForwardUrl("/hello")//请求成功forward跳转路径 跳转之后地址不变
                //.defaultSuccessUrl("/index",true)//默认认证成功之后的跳转，跳转之后地址发生改变
                .successHandler(new MyAuthenticationSuccessHandler())//认证成功时，前后端分离的处理方案
                //.failureForwardUrl("/login.html")//认证失败之后的forward跳转
                //.failureUrl("/login.html")//认证失败之后的redirect跳转
                .failureHandler(new MyAuthenticationFailureHandler())//用来自定义认证失败之后的处理,前后端分离解决方案
                .and()
                .logout()
                //.logoutUrl("/logout")//指定注销登录url
                .logoutRequestMatcher(new OrRequestMatcher(
                        new AntPathRequestMatcher("/aa","GET"),
                        new AntPathRequestMatcher("/bb","POST")
                ))
                .invalidateHttpSession(true)//默认的session失效
                .clearAuthentication(true)//清楚 当前的认证标记
                //.logoutSuccessUrl("/login.html")//注销登录 成功之后的跳转页面
                .logoutSuccessHandler(new MyLogoutSuccessHandler())//注销登录成功之后的处理 前后端分离
                .and()
                .csrf().disable();//禁止csrf跨站请求保护
    }
}
