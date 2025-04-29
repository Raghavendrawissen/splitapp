package org.example.splitapp.model;

import java.util.List;

public class ExpenseWithParticipants {
    private Expense expense;
    private List<ExpenseParticipant> participants;

    public Expense getExpense() {
        return expense;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    public List<ExpenseParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ExpenseParticipant> participants) {
        this.participants = participants;
    }
}
