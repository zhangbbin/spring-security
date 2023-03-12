package com.bin;

import com.bin.domain.User;
import com.bin.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@SpringBootTest
public class TestMapper {
    private final UserMapper userMapper;

    @Autowired
    public TestMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Test
    public void testBcrypt(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //String encode = passwordEncoder.encode("123");
        boolean matches = passwordEncoder.matches("123", "$2a$10$4x.wYpOA6mZeBRfBmd4diuhCZO/4C5.Ps846mi6AWN61TaHOhWRl.");
        System.out.println(matches);

    }

    @Test
    public void testMapper(){
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }
}
