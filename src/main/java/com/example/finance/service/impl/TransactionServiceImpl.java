package com.example.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.finance.dto.MonthlyReportDTO;
import com.example.finance.dto.TransactionReqDTO;
import com.example.finance.entity.Transaction;
import com.example.finance.mapper.TransactionMapper;
import com.example.finance.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 交易流水业务实现。
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class TransactionServiceImpl implements TransactionService {

    private static final Integer DEFAULT_USER_ID = 1;

    private static final Pattern MONTH_PATTERN = Pattern.compile("^\\d{4}-\\d{2}$");

    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    private final TransactionMapper transactionMapper;

    @Override
    public void add(TransactionReqDTO dto) {
        validateType(dto.getType());
        if (dto.getCategory() == null || dto.getCategory().isBlank()) {
            throw new IllegalArgumentException("分类不能为空");
        }

        Transaction transaction = new Transaction();
        transaction.setUserId(DEFAULT_USER_ID);
        transaction.setAmount(dto.getAmount());
        transaction.setType(dto.getType());
        transaction.setCategory(dto.getCategory());
        transaction.setDescription(dto.getDescription());
        transaction.setTransactionDate(dto.getTransactionDate());
        transactionMapper.insert(transaction);
    }

    @Override
    public void delete(Integer id) {
        Transaction transaction = getRequiredTransaction(id);
        transactionMapper.deleteById(transaction.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Transaction getById(Integer id) {
        return getRequiredTransaction(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transaction> listByMonth(Integer userId, String month, String category) {
        validateMonth(month);
        Integer currentUserId = normalizeUserId(userId);

        LambdaQueryWrapper<Transaction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Transaction::getUserId, currentUserId)
                .apply("DATE_FORMAT(transaction_date, '%Y-%m') = {0}", month)
                .eq(category != null && !category.isBlank(), Transaction::getCategory, category)
                .orderByDesc(Transaction::getTransactionDate)
                .orderByDesc(Transaction::getId);
        return transactionMapper.selectList(wrapper);
    }

    @Override
    @Transactional(readOnly = true)
    public MonthlyReportDTO getMonthlyReport(Integer userId, String month) {
        validateMonth(month);
        Integer currentUserId = normalizeUserId(userId);

        BigDecimal totalIncome = transactionMapper.sumByType(currentUserId, month, 1);
        BigDecimal totalExpense = transactionMapper.sumByType(currentUserId, month, 0);

        MonthlyReportDTO report = new MonthlyReportDTO();
        report.setTotalIncome(nullToZero(totalIncome));
        report.setTotalExpense(nullToZero(totalExpense));
        report.setBalance(report.getTotalIncome().subtract(report.getTotalExpense()));
        report.setCategoryStats(transactionMapper.groupByCategory(currentUserId, month));
        report.setDailyTrend(buildDailyTrend(transactionMapper.dailyTrend(currentUserId, month)));
        return report;
    }

    private Transaction getRequiredTransaction(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("交易ID不能为空");
        }
        Transaction transaction = transactionMapper.selectById(id);
        if (transaction == null) {
            throw new IllegalArgumentException("交易记录不存在");
        }
        return transaction;
    }

    private Map<String, BigDecimal> buildDailyTrend(List<Map<String, Object>> rows) {
        Map<String, BigDecimal> trend = new LinkedHashMap<>();
        for (Map<String, Object> row : rows) {
            Object day = row.get("day");
            Object total = row.get("total");
            trend.put(String.valueOf(day), total == null ? BigDecimal.ZERO : new BigDecimal(String.valueOf(total)));
        }
        return trend;
    }

    private void validateType(Integer type) {
        if (type == null || (type != 0 && type != 1)) {
            throw new IllegalArgumentException("交易类型只能是1收入或0支出");
        }
    }

    private void validateMonth(String month) {
        if (month == null || !MONTH_PATTERN.matcher(month).matches()) {
            throw new IllegalArgumentException("月份格式必须为yyyy-MM，例如2026-06");
        }
        YearMonth.parse(month, MONTH_FORMATTER);
    }

    private Integer normalizeUserId(Integer userId) {
        return userId == null ? DEFAULT_USER_ID : userId;
    }

    private BigDecimal nullToZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
