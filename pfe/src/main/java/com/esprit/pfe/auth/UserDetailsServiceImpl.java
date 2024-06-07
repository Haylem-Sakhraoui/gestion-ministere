package com.esprit.pfe.auth;

import com.esprit.pfe.entity.Admin;
import com.esprit.pfe.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Admin user= adminRepository.findByEmail(email).orElse(null);

        if(user==null) {
            throw new UsernameNotFoundException(email + "not found");
        }

        UserDetails u= new org.springframework.security.core.userdetails.User(email,
                user.getPassword(),
                user.getEnabled(),
                true,
                true,
                true,
                getAuthority(user));
        System.out.println("u.toString()==> "+ u.toString());

        if (!u.isEnabled()) {
            throw new DisabledException("Account is disabled");
        }
        return u;
    }

    private Set<SimpleGrantedAuthority> getAuthority(Admin user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().toString()));
        return authorities;
    }
}