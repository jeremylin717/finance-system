package com.example.finance.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 统一响应状态码。
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(200, "成功"),
    PARAM_ERROR(400, "参数错误"),
    NOT_FOUND(404, "资源不存在"),
    SERVER_ERROR(500, "服务器错误");

    private final Integer code;

    private final String msg;
}
