package com.dddd.sldocs.auth.config;

import com.dddd.sldocs.auth.user_details.UserDetailsServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.dao.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@Log4j2
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login_error_disabled", "login_error", "/login_error_bad_credentials",
                        "/login", "/registration", "/resources/templates/registration.html", "/resources/**",
                        "/css/**").permitAll()
                .antMatchers("/teacher/downloadIp").hasAnyAuthority("USER")
                .antMatchers("/teacher/downloadPsl").hasAnyAuthority("USER")
                .antMatchers("/**").hasAnyAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .failureHandler((request, response, exception) -> {
                    log.error("Login failed");
                    log.error(exception);

                    if (exception.toString().equals("org.springframework.security.authentication.DisabledException: User is disabled")) {
                        response.sendRedirect("/login_error_disabled");
                        return;
                    }
                    if (exception.toString().equals("org.springframework.security.authentication.BadCredentialsException: Bad credentials")) {
                        response.sendRedirect("/login_error_bad_credentials");
                        return;
                    }
                    response.sendRedirect("/login_error");
                })
                .defaultSuccessUrl("/")
                .permitAll()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/403")
        ;
    }
}