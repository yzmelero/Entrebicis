package cat.copernic.ymelero.entrebicis.logic.web;

import java.time.LocalDate;
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

    public Usuari crearUsuari(Usuari usuari) throws RuntimeException {
        if (usuariRepository.findByEmail(usuari.getEmail()).isPresent()) {
            throw new RuntimeException("Ja existeix un usuari amb el correu: " + usuari.getEmail());
        }
        if (usuari.getNom() == null || usuari.getNom().isEmpty()) {
            throw new RuntimeException("El nom de l'usuari és obligatori.");
        }
        if (usuari.getCognoms() == null || usuari.getCognoms().isEmpty()) {
            throw new RuntimeException("Els cognoms de l'usuari són obligatoris.");
        }
        if (usuari.getDataNaixement() == null) {
            throw new RuntimeException("La data de naixement de l'usuari és obligatòria.");
        }
        if (usuari.getTelefon() == null || usuari.getTelefon().isEmpty()) {
            throw new RuntimeException("El telèfon de l'usuari és obligatori.");
        }
        if (usuari.getRol() == null) {
            throw new RuntimeException("El rol de l'usuari és obligatori.");
        }
        if (usuari.getContrasenya() == null || usuari.getContrasenya().isEmpty()) {
            throw new RuntimeException("La contrasenya de l'usuari és obligatòria.");
        }
        if (usuari.getSaldo() == null) {
            usuari.setSaldo(0.0);
        }
        if (usuari.getDataAlta() == null) {
            usuari.setDataAlta(LocalDate.now());
        }
        return usuariRepository.save(usuari);
    }

    public Usuari getUsuari(String email) {
        return usuariRepository.findByEmail(email).orElse(null);
    }
}
