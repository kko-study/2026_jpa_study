package com.example.anika.homework.chapter12.repository;

import com.example.anika.homework.chapter12.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
