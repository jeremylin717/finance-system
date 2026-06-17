package com.example.finance.service;

import com.example.finance.dto.LoginDTO;
import com.example.finance.dto.RegisterDTO;
import com.example.finance.vo.LoginUserVO;

/**
 * 登录注册业务接口。
 */
public interface AuthService {

    LoginUserVO register(RegisterDTO dto);

    LoginUserVO login(LoginDTO dto);
}
