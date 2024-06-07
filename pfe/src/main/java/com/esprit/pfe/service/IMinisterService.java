package com.esprit.pfe.service;

import com.esprit.pfe.DTO.MinisterRequest;
import com.esprit.pfe.entity.Minister;

import java.util.List;
import java.util.Optional;

public interface IMinisterService {
    Minister addMinister(MinisterRequest ministerRequest) ;

    // New updateMinister method
    Minister updateMinister(Long id, MinisterRequest ministerRequest);

    List<Minister> findAll();

    Optional<Minister> findById(Long id);

    void deleteById(Long id);
}
