package com.esprit.pfe.controller;

import com.esprit.pfe.DTO.*;
import com.esprit.pfe.entity.Admin;
import com.esprit.pfe.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    @PostMapping("/adminadd")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public AuthenticationResponse superAdminAddAdmin(@RequestBody RegisterRequest request) throws MessagingException, jakarta.mail.MessagingException {
        return adminService.SuperAdminAddAdmin(request);
    }
    @GetMapping("/currentUser")
    public ResponseEntity<Admin> getCurrentUser(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7); // Remove "Bearer " prefix
        Optional<Admin> currentUser = adminService.getCurrentAdmin(jwtToken);
        return currentUser.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/forgetPassword")
    public void forgetPassword(@RequestBody ForgetPassword request) throws Exception  {
        adminService.forgetPassword(request.getEmail());
    }
    @PostMapping("/resetPassword")
    public void resetPassword(@RequestBody resetPasswordRequest request)  {
        adminService.resetPassword(request);
    }
    @PostMapping("/disable")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<String> disableAdmin(@RequestBody abilityRequest request) {
        try {
            adminService.disableAdmin(request);
            return ResponseEntity.ok("Admin disabled successfully.");
        } catch (AdminService.UnauthorizedUserException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping("/enable")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<String> enableAdmin(@RequestBody abilityRequest request) {
        try {
            adminService.enableAdmin(request);
            return ResponseEntity.ok("Admin enabled successfully.");
        } catch (AdminService.UnauthorizedUserException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<Admin>> findAll() {
        List<Admin> adminList = adminService.findAll();
        return ResponseEntity.ok(adminList);
    }

    @GetMapping("/get")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Admin> findById(Long id) {
        Optional<Admin> admin = adminService.findById(id);
        return admin.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteById(Long id) {
        adminService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
