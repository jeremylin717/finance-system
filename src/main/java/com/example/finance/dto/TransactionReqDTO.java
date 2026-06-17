package com.example.finance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 新增交易请求参数。
 */
@Data
@Schema(description = "新增交易请求")
public class TransactionReqDTO {

    @NotNull(message = "金额不能为空")
    @DecimalMin(value = "0.01", message = "金额必须大于0")
    @Schema(description = "交易金额", example = "35.50", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal amount;

    @NotNull(message = "交易类型不能为空")
    @Schema(description = "交易类型，1收入，0支出", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer type;

    @NotBlank(message = "分类不能为空")
    @Schema(description = "分类名称", example = "餐饮", requiredMode = Schema.RequiredMode.REQUIRED)
    private String category;

    @Schema(description = "交易说明", example = "午餐")
    private String description;

    @NotNull(message = "交易日期不能为空")
    @Schema(description = "交易日期", example = "2026-06-17", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate transactionDate;
}
