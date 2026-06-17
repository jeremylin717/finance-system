package com.example.finance.dto;

import com.example.finance.vo.CategorySumVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 月度收支报表。
 */
@Data
@Schema(description = "月度报表")
public class MonthlyReportDTO {

    @Schema(description = "总收入", example = "5000.00")
    private BigDecimal totalIncome;

    @Schema(description = "总支出", example = "2300.00")
    private BigDecimal totalExpense;

    @Schema(description = "结余", example = "2700.00")
    private BigDecimal balance;

    @Schema(description = "按分类统计支出")
    private List<CategorySumVO> categoryStats;

    @Schema(description = "每日支出趋势，key为日期yyyy-MM-dd，value为当天支出合计")
    private Map<String, BigDecimal> dailyTrend;
}
