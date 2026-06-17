package com.example.finance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 注册请求。
 */
@Data
@Schema(description = "注册请求")
public class RegisterDTO {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 30, message = "用户名长度必须在3到30位之间")
    @Schema(description = "用户名", example = "demo", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 30, message = "密码长度必须在6到30位之间")
    @Schema(description = "密码", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @Schema(description = "昵称", example = "小林")
    private String nickname;
}
