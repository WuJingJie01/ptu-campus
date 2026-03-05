package com.campus.security.filter;

import com.campus.security.entity.LoginUser;
import com.campus.security.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class GatewayAuthFilter extends OncePerRequestFilter {

    private static final String USER_ID_HEADER = "X-User-Id";
    private final CustomUserDetailsService customUserDetailsService;

    public GatewayAuthFilter(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String userIdStr = request.getHeader(USER_ID_HEADER);

        if (StringUtils.hasText(userIdStr)) {
            try {
                Long userId = Long.parseLong(userIdStr);
                LoginUser loginUser = customUserDetailsService.loadUserByUserId(userId);

                UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                log.debug("Set authentication for user: {}", userId);
            } catch (NumberFormatException e) {
                log.warn("Invalid X-User-Id header: {}", userIdStr);
            } catch (Exception e) {
                log.error("Failed to load user by id: {}", userIdStr, e);
            }
        }

        filterChain.doFilter(request, response);
    }
}
