package com.example.finance.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 分类金额汇总。
 */
@Data
@Schema(description = "分类统计")
public class CategorySumVO {

    @Schema(description = "分类名称", example = "餐饮")
    private String category;

    @Schema(description = "分类合计金额", example = "568.30")
    private BigDecimal total;
}
