package com.example.finance.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 超支检查结果。
 */
@Data
@Schema(description = "超支分类")
public class OverBudgetVO {

    @Schema(description = "分类名称", example = "餐饮")
    private String category;

    @Schema(description = "月份", example = "2026-06")
    private String month;

    @Schema(description = "预算上限", example = "1000.00")
    private BigDecimal monthlyLimit;

    @Schema(description = "实际支出", example = "1200.00")
    private BigDecimal actualExpense;

    @Schema(description = "超支金额", example = "200.00")
    private BigDecimal overAmount;
}
