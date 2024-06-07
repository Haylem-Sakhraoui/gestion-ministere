package com.esprit.pfe.service;

import com.esprit.pfe.DTO.*;
import com.esprit.pfe.config.JwtService;
import com.esprit.pfe.entity.Admin;
import com.esprit.pfe.entity.Token;
import com.esprit.pfe.repository.AdminRepository;
import com.esprit.pfe.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor

public class AdminService implements IAdminService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserDetailsService userDetailsService;

    private final String clientUrl = "http://localhost:4200/resetPassword";


    @Override
    public AuthenticationResponse SuperAdminAddAdmin(RegisterRequest request) throws MessagingException, jakarta.mail.MessagingException {
        // Check if the Admin already exists
        if (userAlreadyExist(request.getEmail())) {
            throw new UnauthorizedUserException("Admin with email " + request.getEmail() + " already exists.");
        }
        // Generate a random password
        String generatedPassword = generateRandomPassword();

        var admin = Admin.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(generatedPassword))
                .role(request.getRole())
                .enabled(request.getEnable())
                .build();

        adminRepository.save(admin);

        var jwtToken = jwtService.generateToken(new HashMap<>(),admin);

        // Send an email to the user with the password
        String subject = "Welcome ";
        String body = "Dear admin " + admin.getEmail() + ",\n\n"
                + "Welcome to our new platform! Your account has been successfully created.\n\n"
                + "Your temporary password is: " + generatedPassword + "\n\n"
                + "For security reasons, we recommend changing your password after logging in.\n\n"
                + "Thank you for joining us!";
        Mail mail = new Mail(admin.getEmail(), subject, body);
        emailService.sendMail(mail);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
    @Override
    public List<Admin> findAll() {
        return adminRepository.findAll();
    }
    @Override
    public Optional<Admin> findById(Long id) {
        return adminRepository.findById(id);
    }
    @Override
    public void deleteById(Long id) {
        adminRepository.deleteById(id);
    }

    @Override
    public void deleteUserByEmail(String email) {
        Optional<Admin> user = adminRepository.findByEmail(email);

        if (user.isPresent()) {
            adminRepository.deleteById(user.get().getId());
        } else {
            throw new NotFoundException("Admin not found");   }
    }

    @Override
    public Optional<Admin> getCurrentAdmin(String token) {
        String username = jwtService.extractUsername(token);
        return adminRepository.findByEmail(username);
    }

    @Override
    public void resetPassword(resetPasswordRequest request) {
        // Retrieve user by email
        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Check if the current password matches
        if (!passwordEncoder.matches(request.getCurrentPassword(), admin.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        // Check if the new password matches the confirmed password
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }

        // Update user's password
        admin.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // Save the updated user
        adminRepository.save(admin);
    }

    @Override
    public void forgetPassword(String email) throws jakarta.mail.MessagingException {
        Optional<Admin> optionalUser = adminRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            Admin admin =optionalUser.get();
            // Generate a new random password

            final UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            String token = jwtService.generateToken(new HashMap<>(),userDetails);

            // Create a new Token object and set the generated token
            Token userToken = admin.getToken();
            if (userToken == null) {
                userToken = new Token();
                admin.setToken(userToken);
            }
            userToken.setToken(token);

            // Save the Token object first
            tokenRepository.save(userToken);

            String newPassword = generateRandomPassword();

            // Encrypt the new password
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(newPassword);

            // Update the user's password
            admin.setPassword(encodedPassword);

            adminRepository.save(admin);

            final String subject = "Reset Password";
            String url = clientUrl;
            String body = "<div><h3>Hello " + admin.getEmail() + " </h3>" +
                    "<br>" +
                    "<h4>A password reset request has been requested for your user account  </h4>" +
                    "<h4>This your new password : "+ newPassword + "</h4>" +                    "<h4>If you need help, please contact the website administrator.</h4>" +
                    "<br>" +
                    "<h4>Admin</h4></div>";
            Mail mail = new Mail(email, subject, body);
            emailService.sendMail(mail);
        } else {
            // Log the error
            System.out.println("User not found for email: " + email);
            throw new NotFoundException("User not found");
        }
    }


    @Override
    public void disableAdmin(abilityRequest request) {
        // Retrieve the Admin by email
        Optional<Admin> optionalUser = adminRepository.findByEmail(request.getEmail());
        if (optionalUser.isPresent()) {
            Admin Admin = optionalUser.get();
            Admin.setEnabled(false);
            adminRepository.save(Admin);
        } else {
            throw new UnauthorizedUserException("Admin with email " + request.getEmail() + " not found.");
        }
    }

    @Override
    public void enableAdmin(abilityRequest request) {

        // Retrieve the Admin by email
        Optional<Admin> optionalUser = adminRepository.findByEmail(request.getEmail());
        if (optionalUser.isPresent()) {
            Admin Admin = optionalUser.get();
            Admin.setEnabled(true); // Enable the Admin
            adminRepository.save(Admin);
        } else {
            throw new UnauthorizedUserException("Admin with email " + request.getEmail() + " not found.");
        }
    }

    public static class UnauthorizedUserException extends RuntimeException {
        public UnauthorizedUserException(String message) {
            super(message);
        }
    }

    private boolean userAlreadyExist(String email){
        Optional<Admin> Admin = adminRepository.findByEmail(email);
        return Admin.isPresent();
    }

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 10;
    private static final Random RANDOM = new SecureRandom();
    private String generateRandomPassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            password.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return password.toString();
    }

}
