package com.example.finance.controller;

import com.example.finance.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 系统首页和健康检查接口。
 */
@RestController
@Tag(name = "系统接口")
public class HomeController {

    /**
     * 健康检查接口，用于确认服务已正常启动。
     */
    @Operation(summary = "健康检查")
    @GetMapping("/api/health")
    public Result<String> health() {
        return Result.success("finance-system is running");
    }
}
