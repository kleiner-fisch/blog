package com.example.demo.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import com.example.demo.model.CustomUser;
import com.example.demo.service.impl.CustomUserDetailServiceImpl;


import static com.example.demo.service.DefaultValues.ADMIN_ROLE;
import static com.example.demo.service.DefaultValues.USER_ROLE;

@Configuration
@EnableWebSecurity
// @EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

  @Autowired
  private CustomUserDetailServiceImpl userDetailService;

// @Autowired
// private PasswordEncoder passwordEncoder;

private static final String[] AUTH_WHITELIST = {
    // -- Swagger UI v2
    "/v2/api-docs",
    "/swagger-resources",
    "/swagger-resources/**",
    "/configuration/ui",
    "/configuration/security",
    "/swagger-ui.html",
    "/webjars/**",
    // -- Swagger UI v3 (OpenAPI)
    "/v3/api-docs/**",
    "/swagger-ui/**"
    // other public endpoints of your API may be appended to this array
};

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

    httpSecurity.authorizeHttpRequests(registry -> 
    {
      // everybody can create users
      registry.requestMatchers(HttpMethod.POST, "/users").permitAll()
          // everybody can view everything
          .requestMatchers(HttpMethod.GET, "/users", "/users/**", "/posts", "/posts/**").permitAll()
          // // everybody can post comments
          .requestMatchers(new RegexRequestMatcher("/posts/[0-9]+/comments", HttpMethod.POST.toString())).permitAll()
          // .requestMatchers(HttpMethod.POST, "/posts/[0-9]+/comments").permitAll();
          // // users and admins can create posts
          .requestMatchers(HttpMethod.POST, "/posts").hasAnyRole(ADMIN_ROLE, USER_ROLE)
          // .requestMatchers(HttpMethod.GET, "/admin").hasRole(ADMIN_ROLE)
          // // users and admins can delete users
          .requestMatchers( new RegexRequestMatcher("/users/[0-9]+", HttpMethod.DELETE.toString())).hasAnyRole(ADMIN_ROLE, USER_ROLE)
          // // As comments currently have arbitrary authors, only admins can remove them
          .requestMatchers(new RegexRequestMatcher("/posts/[0-9]+/comments/*", HttpMethod.DELETE.toString())).hasRole(ADMIN_ROLE)
          .requestMatchers(new RegexRequestMatcher("/posts/[0-9]+", HttpMethod.DELETE.toString())).hasAnyRole(ADMIN_ROLE, USER_ROLE)
          .requestMatchers( AUTH_WHITELIST).hasAnyRole(ADMIN_ROLE, USER_ROLE);
    });

    httpSecurity.csrf(AbstractHttpConfigurer::disable);
    DefaultSecurityFilterChain filterChain = httpSecurity.httpBasic(Customizer.withDefaults()).build();
    return filterChain;
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
//       .roles(CustomUser.USER_ROLE)
//       .build();
//       UserDetails admin = User.withUsername("admin")
//       .passwordEncoder(pw -> getPasswordEncoder().encode(pw))
//       .password("pw")
//         .roles(CustomUser.ADMIN_ROLE, CustomUser.USER_ROLE)
//         .build();
//     return new InMemoryUserDetailsManager(user, admin);
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

    //   @Bean
    // public PasswordEncoder getPasswordEncoder() {
    //     return NoOpPasswordEncoder.getInstance();
    // }

}