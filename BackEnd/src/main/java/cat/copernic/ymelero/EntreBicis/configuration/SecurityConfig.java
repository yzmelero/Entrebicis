package cat.copernic.ymelero.entrebicis.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        private ValidadorUsuari validadorUsuari;

        @Bean
        public SecurityFilterChain webSecurity(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .securityMatcher("/**")
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/api/**").permitAll()
                                                .requestMatchers("/css/**", "/img/**", "/login").permitAll() // Recursos
                                                .requestMatchers("/**").hasAnyRole("ADMIN") // Todo lo demás requiere
                                                .anyRequest().authenticated())
                                .formLogin(login -> login
                                                .loginPage("/login") // Página de login personalizada
                                                .defaultSuccessUrl("/usuaris", true) // Redirigir a llista d'usuaris
                                                .failureUrl("/login?error=true") // Redirigir a login con error
                                                .usernameParameter("email") // Correo como usuario
                                                .passwordParameter("password") // Nombre del campo de contraseña
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login?logout=true") // Redirigir a login tras logout
                                                .invalidateHttpSession(true) // Invalidar la sesión
                                                .deleteCookies("JSESSIONID") // Eliminar cookies de sesión
                                                .permitAll())
                                .exceptionHandling(handling -> handling
                                                .accessDeniedPage("/error"));
                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @SuppressWarnings("removal")
        @Bean
        public AuthenticationManager authManager(HttpSecurity http) throws Exception {
                return http.getSharedObject(AuthenticationManagerBuilder.class)
                                .userDetailsService(validadorUsuari)
                                .passwordEncoder(passwordEncoder())
                                .and()
                                .build();
        }
}