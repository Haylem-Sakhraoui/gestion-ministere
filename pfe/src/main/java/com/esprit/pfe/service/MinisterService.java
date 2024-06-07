package com.esprit.pfe.service;

import com.esprit.pfe.DTO.MinisterRequest;
import com.esprit.pfe.config.JwtService;
import com.esprit.pfe.entity.Admin;
import com.esprit.pfe.entity.Minister;
import com.esprit.pfe.repository.AdminRepository;
import com.esprit.pfe.repository.MinisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class MinisterService implements IMinisterService{
    @Autowired
    private MinisterRepository ministerRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private JwtService jwtService;

    @Override
    public Minister addMinister(MinisterRequest ministerRequest) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // Find the admin by email
        Admin admin = adminRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Admin not found"));

        // Create the new Minister entity
        Minister minister = new Minister();
        minister.setName(ministerRequest.getName());
        minister.setAdmin(admin);

        // Save the Minister entity
        minister = ministerRepository.save(minister);

        // Update the Admin entity with the newly created Minister
        admin.setMinister(minister);
        adminRepository.save(admin);

        return minister;
    }

    @Override
    // New updateMinister method
    public Minister updateMinister(Long id, MinisterRequest ministerRequest) {
        Optional<Minister> optionalMinister = ministerRepository.findById(id);
        if (optionalMinister.isPresent()) {
            Minister minister = optionalMinister.get();
            // Update the minister entity with new values from ministerRequest
            minister.setName(ministerRequest.getName());
            return ministerRepository.save(minister);
        } else {
            throw new NotFoundException("Minister with id " + id + " not found.");
        }
    }

    @Override
    public List<Minister> findAll() {
        return ministerRepository.findAll();
    }
    @Override
    public Optional<Minister> findById(Long id) {
        return ministerRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        ministerRepository.deleteById(id);
    }
}
