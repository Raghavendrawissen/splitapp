package org.example.splitapp.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class ExpenseParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Expense expense;

    @ManyToOne
    private User user;

    private double share;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Expense getExpense() {
        return expense;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getShare() {
        return share;
    }

    public void setShare(double share) {
        this.share = share;
    }

    public void setAmount(BigDecimal bigDecimal) {

    }

    // Getters and Setters
}
