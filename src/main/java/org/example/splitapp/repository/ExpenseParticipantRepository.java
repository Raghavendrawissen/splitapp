package org.example.splitapp.repository;

import org.example.splitapp.model.ExpenseParticipant;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ExpenseParticipantRepository extends JpaRepository<ExpenseParticipant, Long> {}