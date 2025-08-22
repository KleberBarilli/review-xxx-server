package com.idealizer.review_x.security.services;

import com.idealizer.review_x.domain.user.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        String normalizedIdentifier = identifier.toLowerCase();

        var user = (identifier.contains("@")
                ? userRepository.findByEmail(normalizedIdentifier)
                : userRepository.findByName(normalizedIdentifier))
                        .orElseThrow(() -> new UsernameNotFoundException("identifier"));
        return UserDetailsImpl.build(user);
    }
}
