package com.bin.exception;

import org.springframework.security.core.AuthenticationException;

//自定义验证码认证异常
public class KaptchaNotMatchException extends AuthenticationException  {
    public KaptchaNotMatchException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public KaptchaNotMatchException(String msg) {
        super(msg);
    }
}
