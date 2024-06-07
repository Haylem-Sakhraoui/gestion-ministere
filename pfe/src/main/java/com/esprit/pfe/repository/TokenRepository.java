package com.esprit.pfe.repository;

import com.esprit.pfe.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token,Long> {
}