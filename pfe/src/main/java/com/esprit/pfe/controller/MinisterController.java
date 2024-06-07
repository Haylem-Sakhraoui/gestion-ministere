package com.esprit.pfe.controller;

import com.esprit.pfe.DTO.MinisterRequest;
import com.esprit.pfe.entity.Minister;
import com.esprit.pfe.service.MinisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/minister")
public class MinisterController {
    @Autowired
    private MinisterService ministerService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('MINISTER_ADMIN' )")
    public ResponseEntity<Minister> addMinister(@RequestBody MinisterRequest ministerRequest) {
        Minister minister = ministerService.addMinister(ministerRequest);
        return ResponseEntity.ok(minister);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public List<Minister> getAllMinisters() {
        return ministerService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MINISTER_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Minister> getMinisterById(@PathVariable Long id) {
        Optional<Minister> minister = ministerService.findById(id);
        return minister.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('MINISTER_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Void> deleteMinister(@PathVariable Long id) {
        ministerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // New updateMinister endpoint
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('MINISTER_ADMIN')")
    public ResponseEntity<Minister> updateMinister(@PathVariable Long id, @RequestBody MinisterRequest ministerRequest) {
        Minister updatedMinister = ministerService.updateMinister(id, ministerRequest);
        return ResponseEntity.ok(updatedMinister);
    }

}
