package com.example.task1.util;

import com.example.task1.domain.Owner;
import com.example.task1.repository.OwnerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.List;

@Component
public class DatabaseInitializer {

    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseInitializer(OwnerRepository ownerRepository, PasswordEncoder passwordEncoder) {
        this.ownerRepository = ownerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener
    public void onApplicationEvent(ApplicationReadyEvent event) {
        updatePasswordsInDatabase();
    }

    private void updatePasswordsInDatabase() {
        List<Owner> owners = ownerRepository.findAll();
        for (Owner owner : owners) {
            String password = owner.getPassword();

            if (password == null || password.isBlank()) {
                System.out.println("The User " + owner.getEmail() + " has null password. He needs to set the password manual.");
            } else if (!password.startsWith("$2a$")) {
                owner.setPassword(passwordEncoder.encode(password));
                ownerRepository.save(owner);
                System.out.println("The User password " + owner.getEmail() + " has been encrypted.");
            }
        }
    }
}
