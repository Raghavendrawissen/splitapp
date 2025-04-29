package org.example.splitapp.repository;

import org.example.splitapp.model.Split;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SplitRepository extends JpaRepository<Split, Long> {
}