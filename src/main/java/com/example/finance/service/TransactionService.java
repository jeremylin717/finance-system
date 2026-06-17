package com.example.finance.service;

import com.example.finance.dto.MonthlyReportDTO;
import com.example.finance.dto.TransactionReqDTO;
import com.example.finance.entity.Transaction;

import java.util.List;

/**
 * 交易流水业务接口。
 */
public interface TransactionService {

    void add(TransactionReqDTO dto);

    void delete(Integer id);

    Transaction getById(Integer id);

    List<Transaction> listByMonth(Integer userId, String month, String category);

    MonthlyReportDTO getMonthlyReport(Integer userId, String month);
}
