package com.esprit.pfe.service;

import com.esprit.pfe.DTO.Mail;
import com.esprit.pfe.DTO.claimRequest;
import com.esprit.pfe.entity.Admin;
import com.esprit.pfe.entity.Claim;
import com.esprit.pfe.repository.AdminRepository;
import com.esprit.pfe.repository.ClaimRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;
@Service
public class ClaimService implements  IClaimService{
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ClaimRepository claimRepository ;
    @Autowired
    private EmailService emailService;
    @Override
    public List<Claim> findAll() {
        return claimRepository.findAll();
    }
    @Override
    public Optional<Claim> findById(Long id) {
        return claimRepository.findById(id);
    }
    @Override
    public Claim addClaim(claimRequest claimRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // Find the admin by email
        Admin admin = adminRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Admin not found"));

        Claim claim = new Claim();
        claim.setContent(claimRequest.getContent());
        claim.setAdmin(admin);

        return claimRepository.save(claim);
    }
    @Override
    public void deleteById(Long id) {
        claimRepository.deleteById(id);
    }

    @Override
    public Claim updateContentClaim(long id, claimRequest claimRequest){
        Optional<Claim> claimOptional = claimRepository.findById(id);
        if (claimOptional.isPresent()) {
            Claim claim = claimOptional.get();
            // Update the minister entity with new values from ministerRequest
            claim.setContent(claimRequest.getContent());
            return claimRepository.save(claim);
        } else {
            throw new NotFoundException("Minister with id " + id + " not found.");
        }
    }
    @Override
    public Claim updateStatusClaim(long id, claimRequest claimRequest) throws MessagingException {
        Optional<Claim> claimOptional = claimRepository.findById(id);

        if (claimOptional.isPresent()) {
            Claim claim = claimOptional.get();
            // Update the claim status
            claim.setStatus(claimRequest.getStatus());
            Claim updatedClaim = claimRepository.save(claim);

            // Send email notification
            final String subject = "Claim Status";
            String body =
                    "<div>" +
                            "<h3>Hello </h3>" +
                            "<br>" +
                            "<h4>Your claim id: " + claim.getId() + " is " + claim.getStatus() + "</h4>" +
                            "<h4>If you steel need help, please contact the administrator.</h4>" +
                            "<br>" +
                            "<h4>Admin</h4>" +
                            "</div>";

            Mail mail = new Mail(claim.getAdmin().getEmail(), subject, body);
            emailService.sendMail(mail);

            return updatedClaim;
        } else {
            throw new NotFoundException("Claim with id " + id + " not found.");
        }
    }

}
