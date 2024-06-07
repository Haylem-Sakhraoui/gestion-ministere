package com.esprit.pfe.service;

import com.esprit.pfe.DTO.AuthenticationResponse;
import com.esprit.pfe.DTO.RegisterRequest;
import com.esprit.pfe.DTO.abilityRequest;
import com.esprit.pfe.DTO.resetPasswordRequest;
import com.esprit.pfe.entity.Admin;
import org.springframework.messaging.MessagingException;

import java.util.List;
import java.util.Optional;

public interface IAdminService {
    AuthenticationResponse SuperAdminAddAdmin(RegisterRequest request) throws MessagingException, jakarta.mail.MessagingException;

    List<Admin> findAll();

    Optional<Admin> findById(Long id);


    void deleteById(Long id);

    void deleteUserByEmail(String email);

    Optional<Admin> getCurrentAdmin(String token);

    void resetPassword(resetPasswordRequest request);

    void forgetPassword(String email) throws MessagingException, jakarta.mail.MessagingException;

    void disableAdmin(abilityRequest request);

    void enableAdmin(abilityRequest request);
}
