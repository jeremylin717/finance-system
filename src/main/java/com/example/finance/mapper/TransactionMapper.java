package com.example.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.finance.entity.Transaction;
import com.example.finance.vo.CategorySumVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 交易流水 Mapper。
 */
public interface TransactionMapper extends BaseMapper<Transaction> {

    /**
     * 按月份和类型统计收入或支出总额。
     */
    @Select("""
            SELECT COALESCE(SUM(amount), 0)
            FROM `transaction`
            WHERE user_id = #{userId}
              AND DATE_FORMAT(transaction_date, '%Y-%m') = #{month}
              AND type = #{type}
            """)
    BigDecimal sumByType(@Param("userId") Integer userId,
                         @Param("month") String month,
                         @Param("type") Integer type);

    /**
     * 按分类统计指定月份的支出金额。
     */
    @Select("""
            SELECT category, COALESCE(SUM(amount), 0) AS total
            FROM `transaction`
            WHERE user_id = #{userId}
              AND DATE_FORMAT(transaction_date, '%Y-%m') = #{month}
              AND type = 0
            GROUP BY category
            ORDER BY total DESC
            """)
    List<CategorySumVO> groupByCategory(@Param("userId") Integer userId,
                                        @Param("month") String month);

    /**
     * 统计指定月份每日支出趋势。
     */
    @Select("""
            SELECT DATE_FORMAT(transaction_date, '%Y-%m-%d') AS day,
                   COALESCE(SUM(amount), 0) AS total
            FROM `transaction`
            WHERE user_id = #{userId}
              AND DATE_FORMAT(transaction_date, '%Y-%m') = #{month}
              AND type = 0
            GROUP BY transaction_date
            ORDER BY transaction_date ASC
            """)
    List<Map<String, Object>> dailyTrend(@Param("userId") Integer userId,
                                         @Param("month") String month);
}
