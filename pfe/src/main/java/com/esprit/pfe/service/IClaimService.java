package com.esprit.pfe.service;

import com.esprit.pfe.DTO.claimRequest;
import com.esprit.pfe.entity.Claim;
import jakarta.mail.MessagingException;

import java.util.List;
import java.util.Optional;

public interface IClaimService {
    List<Claim> findAll();

    Optional<Claim> findById(Long id);

    Claim addClaim(claimRequest claimRequest);

    void deleteById(Long id);

    Claim updateContentClaim(long id, claimRequest claimRequest);

    Claim updateStatusClaim(long id, claimRequest claimRequest) throws MessagingException;

}
