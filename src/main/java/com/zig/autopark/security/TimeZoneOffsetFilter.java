package com.zig.autopark.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TimeZoneOffsetFilter extends OncePerRequestFilter {
 
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String timeZone = request.getParameter("timeZoneOffset");
        TimeZoneOffsetContext.set(timeZone);
        filterChain.doFilter(request, response);
        TimeZoneOffsetContext.reset();
    }
}