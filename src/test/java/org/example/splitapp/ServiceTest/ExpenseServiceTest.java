package org.example.splitapp.ServiceTest;

import org.example.splitapp.model.Expense;
import org.example.splitapp.model.ExpenseParticipant;
import org.example.splitapp.model.Group;
import org.example.splitapp.repository.ExpenseParticipantRepository;
import org.example.splitapp.repository.ExpenseRepository;
import org.example.splitapp.Service.ExpenseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private ExpenseParticipantRepository participantRepository;

    private ExpenseServiceImpl expenseService;

    @BeforeEach
    void setUp() {
        expenseService = new ExpenseServiceImpl();
        // Set mocked repositories
        org.springframework.test.util.ReflectionTestUtils.setField(expenseService, "expenseRepository", expenseRepository);
        org.springframework.test.util.ReflectionTestUtils.setField(expenseService, "participantRepository", participantRepository);
    }

    @Test
    void addExpense_Success() {
        // Arrange
        Group group = new Group();
        group.setId(1L);

        Expense expense = new Expense();
        expense.setGroup(group);
        expense.setAmount(BigDecimal.valueOf(100.00));
        expense.setDescription("Test Expense");
        expense.setDate(String.valueOf(LocalDateTime.now()));

        ExpenseParticipant participant1 = new ExpenseParticipant();
        participant1.setAmount(BigDecimal.valueOf(50.00));
        ExpenseParticipant participant2 = new ExpenseParticipant();
        participant2.setAmount(BigDecimal.valueOf(50.00));

        List<ExpenseParticipant> participants = Arrays.asList(participant1, participant2);

        Expense savedExpense = new Expense();
        savedExpense.setExpenseId(1L);
        savedExpense.setGroup(group);
        savedExpense.setAmount(expense.getAmount());
        savedExpense.setDescription(expense.getDescription());
        savedExpense.setDate(expense.getDate());

        when(expenseRepository.save(any(Expense.class))).thenReturn(savedExpense);
        when(participantRepository.save(any(ExpenseParticipant.class))).thenReturn(new ExpenseParticipant());

        // Act
        Expense result = expenseService.addExpense(expense, participants);

        // Assert
        assertNotNull(result);
        assertEquals(savedExpense.getExpenseId(), result.getExpenseId());
        assertEquals(savedExpense.getAmount(), result.getAmount());
        assertEquals(savedExpense.getDescription(), result.getDescription());
        verify(expenseRepository, times(1)).save(any(Expense.class));
        verify(participantRepository, times(2)).save(any(ExpenseParticipant.class));
    }

    @Test
    void getExpensesByGroupId_Success() {
        // Arrange
        Long groupId = 1L;
        Group group = new Group();
        group.setId(groupId);

        Expense expense1 = new Expense();
        expense1.setExpenseId(1L);
        expense1.setGroup(group);
        expense1.setAmount(BigDecimal.valueOf(100.00));

        Expense expense2 = new Expense();
        expense2.setExpenseId(2L);
        expense2.setGroup(group);
        expense2.setAmount(BigDecimal.valueOf(150.00));

        List<Expense> expectedExpenses = Arrays.asList(expense1, expense2);

        when(expenseRepository.findByGroupGroupId(groupId)).thenReturn(expectedExpenses);

        // Act
        List<Expense> result = expenseService.getExpensesByGroupId(groupId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedExpenses, result);
        verify(expenseRepository).findByGroupGroupId(groupId);
    }

    @Test
    void getParticipantsByExpenseId_Success() {
        // Arrange
        Long expenseId = 1L;
        Expense expense = new Expense();
        expense.setExpenseId(expenseId);

        ExpenseParticipant participant1 = new ExpenseParticipant();
        participant1.setExpense(expense);
        participant1.setAmount(BigDecimal.valueOf(50.00));

        ExpenseParticipant participant2 = new ExpenseParticipant();
        participant2.setExpense(expense);
        participant2.setAmount(BigDecimal.valueOf(50.00));

        List<ExpenseParticipant> allParticipants = Arrays.asList(
            participant1, 
            participant2,
            // Add participant from different expense to test filtering
            createParticipantWithDifferentExpense()
        );

        when(participantRepository.findAll()).thenReturn(allParticipants);

        // Act
        List<ExpenseParticipant> result = expenseService.getParticipantsByExpenseId(expenseId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> p.getExpense().getExpenseId().equals(expenseId)));
        verify(participantRepository).findAll();
    }

    private ExpenseParticipant createParticipantWithDifferentExpense() {
        Expense differentExpense = new Expense();
        differentExpense.setExpenseId(2L);
        
        ExpenseParticipant participant = new ExpenseParticipant();
        participant.setExpense(differentExpense);
        participant.setAmount(BigDecimal.valueOf(75.00));
        
        return participant;
    }

    @Test
    void addExpense_WithEmptyParticipants() {
        // Arrange
        Expense expense = new Expense();
        expense.setAmount(BigDecimal.valueOf(100.00));
        expense.setDescription("Test Expense");
        
        List<ExpenseParticipant> emptyParticipants = List.of();

        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);

        // Act
        Expense result = expenseService.addExpense(expense, emptyParticipants);

        // Assert
        assertNotNull(result);
        verify(expenseRepository, times(1)).save(any(Expense.class));
        verify(participantRepository, never()).save(any(ExpenseParticipant.class));
    }

    @Test
    void getExpensesByGroupId_EmptyResult() {
        // Arrange
        Long groupId = 1L;
        when(expenseRepository.findByGroupGroupId(groupId)).thenReturn(List.of());

        // Act
        List<Expense> result = expenseService.getExpensesByGroupId(groupId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(expenseRepository).findByGroupGroupId(groupId);
    }
}