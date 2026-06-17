package com.example.finance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 交易流水实体。
 */
@Data
@TableName("`transaction`")
@Schema(description = "交易流水")
public class Transaction {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID", example = "1")
    private Integer id;

    @Schema(description = "用户ID，当前固定为1", example = "1")
    private Integer userId;

    @Schema(description = "交易金额", example = "35.50")
    private BigDecimal amount;

    @Schema(description = "交易类型，1收入，0支出", example = "0")
    private Integer type;

    @Schema(description = "分类名称", example = "餐饮")
    private String category;

    @Schema(description = "交易说明", example = "午餐")
    private String description;

    @Schema(description = "交易日期", example = "2026-06-17")
    private LocalDate transactionDate;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}
