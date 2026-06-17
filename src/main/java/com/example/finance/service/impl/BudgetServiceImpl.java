package com.example.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.finance.entity.Budget;
import com.example.finance.mapper.BudgetMapper;
import com.example.finance.mapper.TransactionMapper;
import com.example.finance.service.BudgetService;
import com.example.finance.vo.OverBudgetVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 预算业务实现。
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class BudgetServiceImpl implements BudgetService {

    private static final Integer DEFAULT_USER_ID = 1;

    private static final Pattern MONTH_PATTERN = Pattern.compile("^\\d{4}-\\d{2}$");

    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    private final BudgetMapper budgetMapper;

    private final TransactionMapper transactionMapper;

    @Override
    public void save(Budget budget) {
        validateBudget(budget, false);
        budget.setUserId(normalizeUserId(budget.getUserId()));

        Budget exists = findByUserCategoryMonth(budget.getUserId(), budget.getCategory(), budget.getMonth());
        if (exists != null) {
            throw new IllegalArgumentException("该分类本月预算已存在");
        }
        budgetMapper.insert(budget);
    }

    @Override
    public void update(Budget budget) {
        validateBudget(budget, true);
        Budget oldBudget = getRequiredBudget(budget.getId());
        budget.setUserId(normalizeUserId(budget.getUserId()));

        Budget exists = findByUserCategoryMonth(budget.getUserId(), budget.getCategory(), budget.getMonth());
        if (exists != null && !exists.getId().equals(budget.getId())) {
            throw new IllegalArgumentException("该分类本月预算已存在");
        }

        oldBudget.setCategory(budget.getCategory());
        oldBudget.setMonth(budget.getMonth());
        oldBudget.setMonthlyLimit(budget.getMonthlyLimit());
        budgetMapper.updateById(oldBudget);
    }

    @Override
    public void delete(Integer id) {
        Budget budget = getRequiredBudget(id);
        budgetMapper.deleteById(budget.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Budget getById(Integer id) {
        return getRequiredBudget(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Budget> listByMonth(Integer userId, String month) {
        validateMonth(month);
        Integer currentUserId = normalizeUserId(userId);
        return budgetMapper.selectList(new LambdaQueryWrapper<Budget>()
                .eq(Budget::getUserId, currentUserId)
                .eq(Budget::getMonth, month)
                .orderByAsc(Budget::getCategory));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OverBudgetVO> checkOverBudget(Integer userId, String month) {
        validateMonth(month);
        Integer currentUserId = normalizeUserId(userId);
        List<Budget> budgets = listByMonth(currentUserId, month);
        List<OverBudgetVO> overBudgets = new ArrayList<>();

        for (Budget budget : budgets) {
            BigDecimal actualExpense = sumExpenseByCategory(currentUserId, month, budget.getCategory());
            if (actualExpense.compareTo(budget.getMonthlyLimit()) > 0) {
                OverBudgetVO vo = new OverBudgetVO();
                vo.setCategory(budget.getCategory());
                vo.setMonth(budget.getMonth());
                vo.setMonthlyLimit(budget.getMonthlyLimit());
                vo.setActualExpense(actualExpense);
                vo.setOverAmount(actualExpense.subtract(budget.getMonthlyLimit()));
                overBudgets.add(vo);
            }
        }
        return overBudgets;
    }

    private BigDecimal sumExpenseByCategory(Integer userId, String month, String category) {
        return transactionMapper.selectList(new LambdaQueryWrapper<com.example.finance.entity.Transaction>()
                        .select(com.example.finance.entity.Transaction::getAmount)
                        .eq(com.example.finance.entity.Transaction::getUserId, userId)
                        .eq(com.example.finance.entity.Transaction::getType, 0)
                        .eq(com.example.finance.entity.Transaction::getCategory, category)
                        .apply("DATE_FORMAT(transaction_date, '%Y-%m') = {0}", month))
                .stream()
                .map(com.example.finance.entity.Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Budget getRequiredBudget(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("预算ID不能为空");
        }
        Budget budget = budgetMapper.selectById(id);
        if (budget == null) {
            throw new IllegalArgumentException("预算不存在");
        }
        return budget;
    }

    private Budget findByUserCategoryMonth(Integer userId, String category, String month) {
        return budgetMapper.selectOne(new LambdaQueryWrapper<Budget>()
                .eq(Budget::getUserId, userId)
                .eq(Budget::getCategory, category)
                .eq(Budget::getMonth, month)
                .last("LIMIT 1"));
    }

    private void validateBudget(Budget budget, boolean requireId) {
        if (budget == null) {
            throw new IllegalArgumentException("预算不能为空");
        }
        if (requireId && budget.getId() == null) {
            throw new IllegalArgumentException("预算ID不能为空");
        }
        if (budget.getCategory() == null || budget.getCategory().isBlank()) {
            throw new IllegalArgumentException("预算分类不能为空");
        }
        validateMonth(budget.getMonth());
        if (budget.getMonthlyLimit() == null || budget.getMonthlyLimit().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("预算金额必须大于0");
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
}
