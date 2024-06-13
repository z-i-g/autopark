package com.zig.autopark.service;

import com.zig.autopark.model.Manager;
import com.zig.autopark.repository.ManagerRepository;
import com.zig.autopark.security.TimeZoneOffsetContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ManagerService implements UserDetailsService {
    private final ManagerRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Manager manager = repository.findByUsername(username).orElse(null);
        return manager.setTimeZoneOffset(TimeZoneOffsetContext.get());
    }
}