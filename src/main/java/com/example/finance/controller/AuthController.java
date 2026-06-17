package com.example.finance.controller;

import com.example.finance.common.Result;
import com.example.finance.dto.LoginDTO;
import com.example.finance.dto.RegisterDTO;
import com.example.finance.service.AuthService;
import com.example.finance.vo.LoginUserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录注册接口。
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "登录注册")
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<LoginUserVO> register(@Valid @RequestBody RegisterDTO dto) {
        return Result.success(authService.register(dto));
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<LoginUserVO> login(@Valid @RequestBody LoginDTO dto) {
        return Result.success(authService.login(dto));
    }
}
