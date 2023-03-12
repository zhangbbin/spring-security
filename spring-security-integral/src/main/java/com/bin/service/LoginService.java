package com.bin.service;

import com.bin.domain.ResponseResult;
import com.bin.domain.User;

public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
