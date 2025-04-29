package org.example.splitapp.Service;

import org.example.splitapp.model.Expense;
import org.example.splitapp.model.ExpenseParticipant;



import java.util.List;

public interface ExpenseService {
    Expense addExpense(Expense expense, List<ExpenseParticipant> participants);
    List<Expense> getExpensesByGroupId(Long groupId);
    List<ExpenseParticipant> getParticipantsByExpenseId(Long expenseId);
}
