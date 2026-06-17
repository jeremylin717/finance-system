package com.example.finance.service;

import com.example.finance.entity.Budget;
import com.example.finance.vo.OverBudgetVO;

import java.util.List;

/**
 * 预算业务接口。
 */
public interface BudgetService {

    void save(Budget budget);

    void update(Budget budget);

    void delete(Integer id);

    Budget getById(Integer id);

    List<Budget> listByMonth(Integer userId, String month);

    List<OverBudgetVO> checkOverBudget(Integer userId, String month);
}
