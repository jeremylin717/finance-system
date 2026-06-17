package com.example.finance.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Controller 统一返回对象。
 *
 * @param <T> data 字段的数据类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "统一返回结果")
public class Result<T> {

    @Schema(description = "状态码", example = "200")
    private Integer code;

    @Schema(description = "响应消息", example = "成功")
    private String msg;

    @Schema(description = "响应数据")
    private T data;

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), data);
    }

    public static <T> Result<T> fail(String msg) {
        return new Result<>(ResultCode.SERVER_ERROR.getCode(), msg, null);
    }

    public static <T> Result<T> fail(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMsg(), null);
    }

    public static <T> Result<T> fail(ResultCode resultCode, String msg) {
        return new Result<>(resultCode.getCode(), msg, null);
    }
}
