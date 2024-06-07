package com.esprit.pfe.controller;

import com.esprit.pfe.DTO.claimRequest;
import com.esprit.pfe.entity.Claim;
import com.esprit.pfe.service.ClaimService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/claim")
public class ClaimController {
    @Autowired
    private ClaimService claimService;
    @PreAuthorize("hasRole('MINISTER_ADMIN')")
    @PostMapping("/add")
    public Claim addReclamation(@RequestBody claimRequest claimRequest) {
        return claimService.addClaim(claimRequest);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<Claim>> findAll() {
        List<Claim> employees = claimService.findAll();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/get")
    @PreAuthorize("hasAnyRole('MINISTER_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Claim> findById(Long id) {
        Optional<Claim> Claim = claimService.findById(id);
        return Claim.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('MINISTER_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Void> deleteById(Long id) {
        claimService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/updateContent/{id}")
    @PreAuthorize("hasRole('MINISTER_ADMIN')")
    public ResponseEntity<Claim> updateContentClaim(@PathVariable Long id, @RequestBody claimRequest claimRequest) {
        Claim updateContentClaim = claimService.updateContentClaim(id, claimRequest);
        return ResponseEntity.ok(updateContentClaim);
    }

    @PutMapping("/updateStatus/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Claim> updateStatusClaim(@PathVariable Long id, @RequestBody claimRequest claimRequest) throws MessagingException {
        Claim updateStatusClaim = claimService.updateStatusClaim(id, claimRequest);
        return ResponseEntity.ok(updateStatusClaim);
    }
}
