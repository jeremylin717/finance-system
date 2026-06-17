package com.example.finance.controller;

import com.example.finance.common.Result;
import com.example.finance.dto.MonthlyReportDTO;
import com.example.finance.dto.TransactionReqDTO;
import com.example.finance.entity.Transaction;
import com.example.finance.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 交易流水接口。
 */
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "交易流水管理")
@RequestMapping("/api/transaction")
public class TransactionController {

    private static final Integer DEFAULT_USER_ID = 1;

    private final TransactionService transactionService;

    @Operation(summary = "添加交易流水")
    @PostMapping("/add")
    public Result<Void> add(@Valid @RequestBody TransactionReqDTO dto) {
        transactionService.add(dto);
        return Result.success();
    }

    @Operation(summary = "删除交易流水")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "交易ID", required = true) @PathVariable Integer id) {
        transactionService.delete(id);
        return Result.success();
    }

    @Operation(summary = "查询交易详情")
    @GetMapping("/{id}")
    public Result<Transaction> getById(@Parameter(description = "交易ID", required = true) @PathVariable Integer id) {
        return Result.success(transactionService.getById(id));
    }

    @Operation(summary = "按月份查询交易流水")
    @GetMapping("/list")
    public Result<List<Transaction>> list(
            @Parameter(description = "月份，格式yyyy-MM", example = "2026-06", required = true)
            @Pattern(regexp = "^\\d{4}-\\d{2}$", message = "月份格式必须为yyyy-MM")
            @RequestParam String month,
            @Parameter(description = "分类名称", example = "餐饮")
            @RequestParam(required = false) String category) {
        return Result.success(transactionService.listByMonth(DEFAULT_USER_ID, month, category));
    }

    @Operation(summary = "查询月度报表")
    @GetMapping("/report")
    public Result<MonthlyReportDTO> report(
            @Parameter(description = "月份，格式yyyy-MM", example = "2026-06", required = true)
            @Pattern(regexp = "^\\d{4}-\\d{2}$", message = "月份格式必须为yyyy-MM")
            @RequestParam String month) {
        return Result.success(transactionService.getMonthlyReport(DEFAULT_USER_ID, month));
    }
}
