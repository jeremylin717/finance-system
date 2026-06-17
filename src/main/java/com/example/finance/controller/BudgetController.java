package com.example.finance.controller;

import com.example.finance.common.Result;
import com.example.finance.entity.Budget;
import com.example.finance.service.BudgetService;
import com.example.finance.vo.OverBudgetVO;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 预算接口。
 */
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "预算管理")
@RequestMapping("/api/budget")
public class BudgetController {

    private final BudgetService budgetService;

    @Operation(summary = "新增预算")
    @PostMapping("/save")
    public Result<Void> save(@Valid @RequestBody Budget budget) {
        budgetService.save(budget);
        return Result.success();
    }

    @Operation(summary = "更新预算")
    @PutMapping("/update")
    public Result<Void> update(@Valid @RequestBody Budget budget) {
        budgetService.update(budget);
        return Result.success();
    }

    @Operation(summary = "删除预算")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "预算ID", required = true) @PathVariable Integer id) {
        budgetService.delete(id);
        return Result.success();
    }

    @Operation(summary = "按月份查询预算列表")
    @GetMapping("/list")
    public Result<List<Budget>> list(
            @Parameter(description = "用户ID", example = "1")
            @RequestParam(required = false) Integer userId,
            @Parameter(description = "月份，格式yyyy-MM", example = "2026-06", required = true)
            @Pattern(regexp = "^\\d{4}-\\d{2}$", message = "月份格式必须为yyyy-MM")
            @RequestParam String month) {
        return Result.success(budgetService.listByMonth(userId, month));
    }

    @Operation(summary = "检查所有分类是否超支")
    @GetMapping("/check")
    public Result<List<OverBudgetVO>> check(
            @Parameter(description = "用户ID", example = "1")
            @RequestParam(required = false) Integer userId,
            @Parameter(description = "月份，格式yyyy-MM", example = "2026-06", required = true)
            @Pattern(regexp = "^\\d{4}-\\d{2}$", message = "月份格式必须为yyyy-MM")
            @RequestParam String month) {
        return Result.success(budgetService.checkOverBudget(userId, month));
    }
}
