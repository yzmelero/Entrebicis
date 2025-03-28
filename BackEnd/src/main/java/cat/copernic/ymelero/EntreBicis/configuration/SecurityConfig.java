package cat.copernic.ymelero.entrebicis.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

        @Autowired
        private ValidadorUsuari validadorUsuari;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/css/**", "/img/**").permitAll()
                                                .requestMatchers("/login").permitAll()
                                                .requestMatchers("/**").hasRole("ADMIN") // Solo el admin puede acceder
                                                .anyRequest().authenticated() // Todo lo demás requiere autenticación
                                )
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
                                                .accessDeniedPage("/error") // Página de error si no tiene
                                                                            // permisos
                                );

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