package cat.copernic.ymelero.entrebicis.configuration;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cat.copernic.ymelero.entrebicis.entity.Usuari;
import cat.copernic.ymelero.entrebicis.repository.UsuariRepository;

@Service
public class ValidadorUsuari implements UserDetailsService {

    private final UsuariRepository usuariRepository;

    public ValidadorUsuari(UsuariRepository usuariRepository) {
        this.usuariRepository = usuariRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Usuari> usuariV = usuariRepository.findByEmail(email);
        if (usuariV.isEmpty()) {
            throw new UsernameNotFoundException("Usuari no trobat: " + email);
        }
        Usuari usuari = usuariV.get();

        if (!"ADMIN".equals(usuari.getRol().name())) {
            throw new UsernameNotFoundException("noAdmin");
        }

        return User.builder()
                .username(usuari.getEmail())
                .password(usuari.getContrasenya())
                .roles(usuari.getRol().name()) // ADMIN o CICLISTA
                .build();
    }
}