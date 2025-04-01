package cat.copernic.ymelero.entrebicis.logic.web;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    public Usuari modificarUsuari(Usuari usuariModificat) throws RuntimeException {
        Optional<Usuari> usuariExistent = usuariRepository.findByEmail(usuariModificat.getEmail());
        if (usuariExistent.isPresent()) {
            Usuari usuariAntic = usuariExistent.get();

            if (usuariModificat.getNom() == null || usuariModificat.getNom().isEmpty()) {
                throw new RuntimeException("El nom de l'usuari és obligatori.");
            }
            if (usuariModificat.getCognoms() == null || usuariModificat.getCognoms().isEmpty()) {
                throw new RuntimeException("Els cognoms de l'usuari són obligatoris.");
            }
            if (usuariModificat.getDataNaixement() == null) {
                throw new RuntimeException("La data de naixement de l'usuari és obligatòria.");
            }
            if (usuariModificat.getTelefon() == null || usuariModificat.getTelefon().isEmpty()) {
                throw new RuntimeException("El telèfon de l'usuari és obligatori.");
            }
            if (usuariModificat.getRol() == null) {
                throw new RuntimeException("El rol de l'usuari és obligatori.");
            }
            if (usuariModificat.getContrasenya() == null || usuariModificat.getContrasenya().isEmpty()) {
                throw new RuntimeException("La contrasenya de l'usuari és obligatòria.");
            }

            usuariAntic.setNom(usuariModificat.getNom());
            usuariAntic.setCognoms(usuariModificat.getCognoms());
            usuariAntic.setDataNaixement(usuariModificat.getDataNaixement());
            usuariAntic.setTelefon(usuariModificat.getTelefon());
            usuariAntic.setRol(usuariModificat.getRol());
            usuariAntic.setContrasenya(usuariModificat.getContrasenya());
            usuariAntic.setPoblacio(usuariModificat.getPoblacio());
            usuariAntic.setObservacions(usuariModificat.getObservacions());

            if (usuariModificat.getFoto() != null && usuariModificat.getFoto().length > 0) {
                usuariAntic.setFoto(usuariModificat.getFoto());
            }

            return usuariRepository.save(usuariAntic);
        } else {
            throw new RuntimeException("No s'ha trobat l'usuari amb el correu: " + usuariModificat.getEmail());
        }
    }
}
