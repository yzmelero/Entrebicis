package cat.copernic.ymelero.entrebicis.logic.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.copernic.ymelero.entrebicis.entity.Usuari;
import cat.copernic.ymelero.entrebicis.repository.UsuariRepository;

@Service
public class UsuariLogica {

    @Autowired
    private UsuariRepository usuariRepository;

    public List<Usuari> getAllUsuaris() {
        return usuariRepository.findAll();
    }

    public Usuari crearUsuari(Usuari usuari) {
        if (usuariRepository.findByEmail(usuari.getEmail()).isPresent()) {
            throw new RuntimeException("Ja existeix un usuari amb el correu: " + usuari.getEmail());
        }
        return usuariRepository.save(usuari);
    }

    public Usuari getUsuari(String email) {
        return usuariRepository.findByEmail(email).orElse(null);
    }
}
