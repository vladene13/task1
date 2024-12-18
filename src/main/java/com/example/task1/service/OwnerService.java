package com.example.task1.service;

import com.example.task1.domain.Owner;
import com.example.task1.repository.OwnerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class OwnerService implements UserDetailsService {

    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;

    public OwnerService(OwnerRepository ownerRepository, PasswordEncoder passwordEncoder) {
        this.ownerRepository = ownerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Owner registerOwner(Owner owner) {
        owner.setPassword(passwordEncoder.encode(owner.getPassword()));
        return ownerRepository.save(owner);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Owner owner = ownerRepository.findByEmail(email);
        if (owner == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(
                owner.getEmail(), owner.getPassword(), new ArrayList<>());
    }
}
