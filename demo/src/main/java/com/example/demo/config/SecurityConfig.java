package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.service.impl.CustomUserDetailServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

  @Autowired
  private CustomUserDetailServiceImpl userDetailService;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(registry -> {
          registry.requestMatchers("/users/**", "/posts/**").hasRole("USER");
          registry.requestMatchers("/admin").hasRole("ADMIN");
          registry.requestMatchers("/index", "/", "/register").permitAll();
    })
        .formLogin(form -> form.defaultSuccessUrl("/posts/home"))
		    .logout((logout) -> logout.permitAll())
        .build();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return userDetailService;
  }

//   @Bean
// public InMemoryUserDetailsManager userDetailsService() {
//     UserDetails user = User.withUsername("user")
//     .passwordEncoder(pw -> getPasswordEncoder().encode(pw))
//     .password("pw")
//       .roles("USER")
//       .build();
//     return new InMemoryUserDetailsManager(user);
// }

  @Bean
  public AuthenticationProvider authentificationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailService);
    provider.setPasswordEncoder(getPasswordEncoder());
    return provider;
  }

  @Bean
  public PasswordEncoder getPasswordEncoder() {
    return new BCryptPasswordEncoder(4);

  }

}