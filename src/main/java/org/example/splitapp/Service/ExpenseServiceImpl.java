package org.example.splitapp.Service;



import org.example.splitapp.model.Expense;
import org.example.splitapp.model.ExpenseParticipant;
import org.example.splitapp.repository.ExpenseParticipantRepository;
import org.example.splitapp.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseParticipantRepository participantRepository;

    @Override
    public Expense addExpense(Expense expense, List<ExpenseParticipant> participants) {
        Expense savedExpense = expenseRepository.save(expense);
        for (ExpenseParticipant participant : participants) {
            participant.setExpense(savedExpense);
            participantRepository.save(participant);
        }
        return savedExpense;
    }

    @Override
    public List<Expense> getExpensesByGroupId(Long groupId) {
        return expenseRepository.findByGroupGroupId(groupId);
    }

    @Override
    public List<ExpenseParticipant> getParticipantsByExpenseId(Long expenseId) {
        return participantRepository.findAll()
                .stream()
                .filter(p -> p.getExpense().getExpenseId().equals(expenseId))
                .toList();
    }
}
