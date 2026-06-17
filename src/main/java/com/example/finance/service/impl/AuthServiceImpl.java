package com.example.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.finance.dto.LoginDTO;
import com.example.finance.dto.RegisterDTO;
import com.example.finance.entity.FinanceUser;
import com.example.finance.mapper.FinanceUserMapper;
import com.example.finance.service.AuthService;
import com.example.finance.vo.LoginUserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 登录注册业务实现。
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class AuthServiceImpl implements AuthService {

    private final FinanceUserMapper financeUserMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public LoginUserVO register(RegisterDTO dto) {
        String username = dto.getUsername().trim();
        if (findByUsername(username) != null) {
            throw new IllegalArgumentException("用户名已存在");
        }

        FinanceUser user = new FinanceUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setNickname(dto.getNickname() == null || dto.getNickname().isBlank() ? username : dto.getNickname().trim());
        financeUserMapper.insert(user);
        return toVO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public LoginUserVO login(LoginDTO dto) {
        FinanceUser user = findByUsername(dto.getUsername().trim());
        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("用户名或密码错误");
        }
        return toVO(user);
    }

    private FinanceUser findByUsername(String username) {
        return financeUserMapper.selectOne(new LambdaQueryWrapper<FinanceUser>()
                .eq(FinanceUser::getUsername, username)
                .last("LIMIT 1"));
    }

    private LoginUserVO toVO(FinanceUser user) {
        LoginUserVO vo = new LoginUserVO();
        vo.setUserId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        return vo;
    }
}
