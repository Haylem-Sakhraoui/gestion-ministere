package com.esprit.pfe.auth;

import com.esprit.pfe.DTO.AuthenticationResponse;
import com.esprit.pfe.DTO.RegisterRequest;
import com.esprit.pfe.DTO.AuthResponse;
import com.esprit.pfe.DTO.AuthenticateRequest;


import com.esprit.pfe.config.JwtService;
import com.esprit.pfe.entity.Admin;
import com.esprit.pfe.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    public AuthenticationResponse register(RegisterRequest request) {

        var admin = Admin.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .enabled(request.getEnable())
                .build();

        adminRepository.save(admin);

        var jwtToken = jwtService.generateToken(new HashMap<>(),admin);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthResponse authenticate(AuthenticateRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            final UserDetails userDetails = userDetailsService
                    .loadUserByUsername(request.getEmail());


            var user = adminRepository.findByEmail(request.getEmail())
                    .orElseThrow();
            var jwtToken = jwtService.generateToken(new HashMap<>(),user);
            return new AuthResponse(200,AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build(), "login successfully");

          /*  return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();*/
        }catch (BadCredentialsException e) {
            return new AuthResponse(404, "Invalid email or password");
        }
        catch (DisabledException d) {
            return new AuthResponse(404, "Account disabled ! check you email to enabled it.");

        }


    }


}
