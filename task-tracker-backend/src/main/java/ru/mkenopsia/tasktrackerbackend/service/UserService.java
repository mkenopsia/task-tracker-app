package ru.mkenopsia.tasktrackerbackend.service;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mkenopsia.tasktrackerbackend.dto.UserSignUpRequest;
import ru.mkenopsia.tasktrackerbackend.dto.UserSignUpResponse;
import ru.mkenopsia.tasktrackerbackend.entity.User;
import ru.mkenopsia.tasktrackerbackend.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User save(User user) {
        if(userRepository.existsByUsername(user.getUsername())) {
            throw new EntityExistsException("datasource.error.user.username_already_taken");
        }

        if(userRepository.existsByEmail(user.getEmail())) {
            throw new EntityExistsException("datasource.error.user.email_already_taken");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return this.userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("datasource.error.user.not_found"));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("USER"))
        );
    }
}
