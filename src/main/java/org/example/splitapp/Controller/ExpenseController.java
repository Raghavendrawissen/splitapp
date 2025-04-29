package org.example.splitapp.Controller;

import org.example.splitapp.model.Expense;
import org.example.splitapp.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseRepository expenseRepository;

    @PostMapping
    public ResponseEntity<Expense> saveExpense(@RequestBody Expense expense) {
        Expense savedExpense = expenseRepository.save(expense);
        calculateAndSaveSplits(savedExpense);
        return ResponseEntity.ok(savedExpense);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<Expense>> getExpensesByGroupId(@PathVariable Long groupId) {
        List<Expense> expenses = expenseRepository.findByGroupGroupId(groupId);
        return ResponseEntity.ok(expenses);
    }

    private void calculateAndSaveSplits(Expense expense) {
        // Implementation for splitting the expense among group participants
        // This would involve:
        // 1. Get all participants in the group
        // 2. Calculate equal splits (or based on specified ratios if implemented)
        // 3. Create and save Split entities for each participant
    }
}