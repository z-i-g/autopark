package com.zig.autopark.model;

import org.springframework.security.core.GrantedAuthority;

public class Roles implements GrantedAuthority {
    @Override
    public String getAuthority() {
        return null;
    }
}