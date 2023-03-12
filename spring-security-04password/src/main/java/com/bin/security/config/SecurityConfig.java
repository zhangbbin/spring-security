package com.bin.security.config;

import com.bin.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailService myUserDetailService;

    //使用passwordEncoder第二种方式
    /*@Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }*/


    //自定义的数据源
//    @Bean
//    public UserDetailsService userDetailsService(){
//        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
//        //第一种passwordEncoder方式
//        inMemoryUserDetailsManager.createUser(User.withUsername("root")
//                .password("{bcrypt}$2a$10$nMljk56Cb6T43Pss18YwZO6WGAtEzyd6uh6ZCpvfr3R6vSVMIZMtG")
//                .roles("admin").build());
////        inMemoryUserDetailsManager.createUser(User.withUsername("root")
////                .password("$2a$10$nMljk56Cb6T43Pss18YwZO6WGAtEzyd6uh6ZCpvfr3R6vSVMIZMtG")
////                .roles("admin").build());
//        return inMemoryUserDetailsManager;
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .csrf().disable();
    }
}
