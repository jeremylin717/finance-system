package com.example.finance.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 登录成功后的用户信息。
 */
@Data
@Schema(description = "登录用户信息")
public class LoginUserVO {

    @Schema(description = "用户ID", example = "1")
    private Integer userId;

    @Schema(description = "用户名", example = "demo")
    private String username;

    @Schema(description = "昵称", example = "小林")
    private String nickname;
}
