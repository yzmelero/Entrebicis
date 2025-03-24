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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login").permitAll() // Permitir acceso a la página de login
                .requestMatchers("/**").hasRole("ADMIN") // Solo el admin puede acceder al resto
                .anyRequest().authenticated() // Todo lo demás requiere autenticación
            )
            .formLogin(login -> login
                .loginPage("/login") // Página de login personalizada
                .defaultSuccessUrl("/", true) // Redirigir a "/" tras login exitoso
                .failureUrl("/login?error=true") // Redirigir a login con error
                .usernameParameter("email") // Correo como usuario
                .passwordParameter("password") // Nombre del campo de contraseña
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true") // Redirigir a login tras logout
                .invalidateHttpSession(true) // Invalidar la sesión
                .deleteCookies("JSESSIONID") // Eliminar cookies de sesión
                .permitAll()
            )
            .exceptionHandling(handling -> handling
                .accessDeniedPage("/errorPermisos") // Página de error si no tiene permisos
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .inMemoryAuthentication()
                .withUser("admin@entrebicis.com")
                .password(passwordEncoder().encode("admin123")) // Cambiar por el real en BD
                .roles("ADMIN")
                .and()
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }
}