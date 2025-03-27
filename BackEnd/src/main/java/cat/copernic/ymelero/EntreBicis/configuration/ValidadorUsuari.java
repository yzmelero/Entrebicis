package cat.copernic.ymelero.entrebicis.configuration;

import cat.copernic.ymelero.entrebicis.entity.Usuari;
import cat.copernic.ymelero.entrebicis.repository.UsuariRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ValidadorUsuari implements UserDetailsService { 

    private final UsuariRepository usuariRepository;

    public ValidadorUsuari(UsuariRepository usuariRepository) {
        this.usuariRepository = usuariRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Usuari> usuariOpt = usuariRepository.findByEmail(email);
        if (usuariOpt.isEmpty()) {
            throw new UsernameNotFoundException("Usuari no trobat: " + email);
        }
        Usuari usuari = usuariOpt.get();
        
        return User.builder()
                .username(usuari.getEmail())
                .password(usuari.getContrasenya())
                .roles(usuari.getRol().name()) // ADMIN o CICLISTA
                .build();
    }
}