package org.example.splitapp.repository;

import org.example.splitapp.model.Debt;

import org.springframework.data.jpa.repository.JpaRepository;


public interface DebtRepository extends JpaRepository<Debt, Long> {}