package com.iwallet.caseProject.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.iwallet.caseProject.service.IwalletUserDetailsService;
import com.iwallet.caseProject.service.TokenBlacklist;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtGenerator tokenGenerator;

    @Autowired
    private IwalletUserDetailsService iwalletUserDetailsService;
    
    @Autowired
    private TokenBlacklist tokenBlacklist;

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
    		jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain)
    				throws jakarta.servlet.ServletException, IOException {
        
        String token = getJWTFromRequest(request);
        if(StringUtils.hasText(token) && tokenGenerator.validateToken(token)) {
            String username = tokenGenerator.getUsernameFromJWT(token);

            UserDetails userDetails = iwalletUserDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                    userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        
    	
        if (token == null || !tokenBlacklist.isBlacklisted(token)) {
            // Token is valid and not blacklisted
            // Proceed with request processing
        	filterChain.doFilter(request, response);
        } else {
            // Token is blacklisted or expired, deny access
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        
    }


    public String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

}
