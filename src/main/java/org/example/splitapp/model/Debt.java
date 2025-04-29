package org.example.splitapp.model;

import jakarta.persistence.*;


@Entity
public class Debt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User owedBy;

    @ManyToOne
    private User owedTo;

    private double amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwedBy() {
        return owedBy;
    }

    public void setOwedBy(User owedBy) {
        this.owedBy = owedBy;
    }

    public User getOwedTo() {
        return owedTo;
    }

    public void setOwedTo(User owedTo) {
        this.owedTo = owedTo;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    // Getters and Setters
}
