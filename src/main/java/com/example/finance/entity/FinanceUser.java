package com.example.finance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统用户实体。
 */
@Data
@TableName("finance_user")
@Schema(description = "系统用户")
public class FinanceUser {

    @TableId(type = IdType.AUTO)
    @Schema(description = "用户ID", example = "1")
    private Integer id;

    @Schema(description = "登录账号", example = "demo")
    private String username;

    @Schema(description = "BCrypt 加密后的密码")
    private String password;

    @Schema(description = "昵称", example = "小林")
    private String nickname;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
}
