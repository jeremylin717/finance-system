package com.example.finance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 月度预算实体。
 */
@Data
@TableName("budget")
@Schema(description = "月度预算")
public class Budget {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID", example = "1")
    private Integer id;

    @Schema(description = "用户ID，当前固定为1", example = "1")
    private Integer userId;

    @NotBlank(message = "预算分类不能为空")
    @Schema(description = "分类名称", example = "餐饮")
    private String category;

    @NotBlank(message = "月份不能为空")
    @Pattern(regexp = "^\\d{4}-\\d{2}$", message = "月份格式必须为yyyy-MM")
    @Schema(description = "月份，格式yyyy-MM", example = "2026-06")
    private String month;

    @NotNull(message = "预算金额不能为空")
    @DecimalMin(value = "0.01", message = "预算金额必须大于0")
    @Schema(description = "月度预算上限", example = "1500.00")
    private BigDecimal monthlyLimit;
}
