package com.iwallet.caseProject.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.iwallet.caseProject.service.IwalletUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	  @Autowired
	  IwalletUserDetailsService userDetailsService;

	  @Autowired
	  private JwtAuthEntryPoint authEntryPoint;

	  @Bean
	  public JwtAuthFilter authJwtTokenFilter() {
	    return new JwtAuthFilter();
	  }

	  public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
	    authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	  }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
		        .csrf(csrf -> {
		            csrf.disable();
		        })
		        .cors(cors -> cors.disable())
		        .exceptionHandling((exception)-> exception.authenticationEntryPoint(authEntryPoint))
		        .authorizeHttpRequests(auth -> {
		            auth.requestMatchers("/iwalletapi/auth/**").permitAll();
		            auth.anyRequest().authenticated();
		        })
		        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public  JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter();
    }
}
