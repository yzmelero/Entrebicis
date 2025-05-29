package cat.copernic.ymelero.entrebicis.logic.web;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cat.copernic.ymelero.entrebicis.entity.Usuari;
import cat.copernic.ymelero.entrebicis.repository.UsuariRepository;

/**
 * Classe de lògica de negoci per gestionar els usuaris.
 */
@Service
public class UsuariLogica {

    @Autowired
    private UsuariRepository usuariRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Obté tots els usuaris.
     *
     * @return Llista d'usuaris.
     */
    public List<Usuari> getAllUsuaris() {
        return usuariRepository.findAll();
    }

    /**
     * Crea un nou usuari.
     *
     * @param usuari L'usuari a crear.
     * @return L'usuari creat.
     * @throws RuntimeException Si hi ha un error en la creació de l'usuari.
     */
    public Usuari crearUsuari(Usuari usuari) throws RuntimeException {
        if (usuariRepository.findByEmail(usuari.getEmail()).isPresent()) {
            throw new RuntimeException("Ja existeix un usuari amb el correu: " + usuari.getEmail());
        }
        if (!usuari.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new RuntimeException("El correu electrònic no té un format vàlid.");
        }

        if (usuari.getNom() == null || usuari.getNom().isEmpty()) {
            throw new RuntimeException("El nom de l'usuari és obligatori.");
        }
        if (!usuari.getNom().matches("^[a-zA-ZÀ-ÿ\\s]*$")) {
            throw new RuntimeException("El nom només pot contenir lletres i espais.");
        }
        if (usuari.getNom().length() > 30) {
            throw new RuntimeException("El nom no pot tenir més de 30 caràcters.");
        }

        if (usuari.getCognoms() == null || usuari.getCognoms().isEmpty()) {
            throw new RuntimeException("Els cognoms de l'usuari són obligatoris.");
        }
        if (!usuari.getCognoms().matches("^[a-zA-ZÀ-ÿ\\s]*$")) {
            throw new RuntimeException("Els cognoms només poden contenir lletres i espais.");
        }
        if (usuari.getCognoms().length() > 80) {
            throw new RuntimeException("Els cognoms no poden tenir més de 80 caràcters.");
        }

        if (usuari.getDataNaixement() == null) {
            throw new RuntimeException("La data de naixement de l'usuari és obligatòria.");
        }
        if (usuari.getDataNaixement().isAfter(LocalDate.now())) {
            throw new RuntimeException("La data de naixement no pot ser una data futura.");
        }
        LocalDate dataMinima = LocalDate.now().minusYears(18);
        if (usuari.getDataNaixement().isAfter(dataMinima)) {
            throw new RuntimeException("Ha de ser major d'edat.");
        }

        if (usuari.getTelefon() == null || usuari.getTelefon().isEmpty()) {
            throw new RuntimeException("El telèfon de l'usuari és obligatori.");
        }
        if (!usuari.getTelefon().matches("\\d{9}")) {
            throw new RuntimeException("El telèfon ha de contenir exactament 9 dígits numèrics.");
        }
        if (usuari.getRol() == null) {
            throw new RuntimeException("El rol de l'usuari és obligatori.");
        }

        if (usuari.getContrasenya() == null || usuari.getContrasenya().isEmpty()) {
            throw new RuntimeException("La contrasenya de l'usuari és obligatòria.");
        }
        if (usuari.getContrasenya().length() < 6) {
            throw new RuntimeException("La contrasenya ha de tenir almenys 6 caràcters.");
        }
        if (!usuari.getContrasenya().matches("^(?=.*[A-Za-z])(?=.*\\d).+$")) {
            throw new RuntimeException("La contrasenya ha de contenir com a mínim una lletra i un número.");
        }

        if (usuari.getPoblacio() != null) {
            if (!usuari.getPoblacio().matches("^[a-zA-ZÀ-ÿ\\s]*$")) {
                throw new RuntimeException("La població només pot contenir lletres i espais.");
            }
            if (usuari.getPoblacio().length() > 20) {
                throw new RuntimeException("La població no pot tenir més de 20 caràcters.");
            }
        }
        if (usuari.getObservacions() != null) {
            if (!usuari.getObservacions().matches("^[a-zA-ZÀ-ÿ0-9.,\\-\\s]*$")) {
                throw new RuntimeException("Les observacions tenen caràcters no valids.");
            }
            if (usuari.getObservacions().length() > 50) {
                throw new RuntimeException("Les observacions no poden tenir més de 50 caràcters.");
            }
        }
        if (usuari.getSaldo() == null) {
            usuari.setSaldo(0.0);
        }
        if (usuari.getDataAlta() == null) {
            usuari.setDataAlta(LocalDate.now());
        }
        if (usuari.getFoto() != null && usuari.getFoto().length > 1_000_000) { // 1 MB
            throw new RuntimeException("La foto és massa gran. Ha de ser inferior a 1MB.");
        }
        return usuariRepository.save(usuari);
    }

    /**
     * Elimina un usuari.
     *
     * @param email L'email de l'usuari a eliminar.
     * @throws RuntimeException Si no s'ha trobat l'usuari o si no es pot eliminar.
     */
    public Usuari getUsuari(String email) {
        return usuariRepository.findByEmail(email).orElse(null);
    }

    /**
     * Modifica un usuari existent.
     *
     * @param usuariModificat L'usuari a modificar.
     * @return L'usuari modificat.
     * @throws RuntimeException Si hi ha un error en la modificació de l'usuari.
     */
    public Usuari modificarUsuari(Usuari usuariModificat) throws RuntimeException {
        Optional<Usuari> usuariExistent = usuariRepository.findByEmail(usuariModificat.getEmail());
        if (usuariExistent.isPresent()) {
            Usuari usuariAntic = usuariExistent.get();

            if (usuariModificat.getNom() == null || usuariModificat.getNom().isEmpty()) {
                throw new RuntimeException("El nom de l'usuari és obligatori.");
            }
            if (!usuariModificat.getNom().matches("^[a-zA-ZÀ-ÿ\\s]*$")) {
                throw new RuntimeException("El nom només pot contenir lletres i espais.");
            }
            if (usuariModificat.getNom().length() > 30) {
                throw new RuntimeException("El nom no pot tenir més de 30 caràcters.");
            }

            if (usuariModificat.getCognoms() == null || usuariModificat.getCognoms().isEmpty()) {
                throw new RuntimeException("Els cognoms de l'usuari són obligatoris.");
            }
            if (!usuariModificat.getCognoms().matches("^[a-zA-ZÀ-ÿ\\s]*$")) {
                throw new RuntimeException("Els cognoms només poden contenir lletres i espais.");
            }
            if (usuariModificat.getCognoms().length() > 80) {
                throw new RuntimeException("Els cognoms no poden tenir més de 80 caràcters.");
            }

            if (usuariModificat.getDataNaixement() == null) {
                throw new RuntimeException("La data de naixement de l'usuari és obligatòria.");
            }
            if (usuariModificat.getDataNaixement().isAfter(LocalDate.now())) {
                throw new RuntimeException("La data de naixement no pot ser una data futura.");
            }
            LocalDate dataMinima = LocalDate.now().minusYears(18);
            if (usuariModificat.getDataNaixement().isAfter(dataMinima)) {
                throw new RuntimeException("Ha de ser major d'edat.");
            }

            if (usuariModificat.getTelefon() == null || usuariModificat.getTelefon().isEmpty()) {
                throw new RuntimeException("El telèfon de l'usuari és obligatori.");
            }
            if (!usuariModificat.getTelefon().matches("\\d{9}")) {
                throw new RuntimeException("El telèfon ha de contenir exactament 9 dígits numèrics.");
            }

            if (!usuariModificat.getObservacions().matches("^[a-zA-ZÀ-ÿ0-9.,\\-\\s]*$")) {
                throw new RuntimeException("Les observacions tenen caràcters no valids.");
            }
            if (usuariModificat.getObservacions().length() > 50) {
                throw new RuntimeException("Les observacions no poden tenir més de 50 caràcters.");
            }

            if (!usuariModificat.getPoblacio().matches("^[a-zA-ZÀ-ÿ\\s]*$")) {
                throw new RuntimeException("La població només pot contenir lletres i espais.");
            }
            if (usuariModificat.getPoblacio().length() > 20) {
                throw new RuntimeException("La població no pot tenir més de 20 caràcters.");
            }

            if (usuariModificat.getRol() == null) {
                throw new RuntimeException("El rol de l'usuari és obligatori.");
            }
            if (usuariModificat.getContrasenya() != null && !usuariModificat.getContrasenya().isEmpty()
                    && !usuariModificat.getContrasenya().equals(usuariAntic.getContrasenya())) {

                if (usuariModificat.getContrasenya().length() < 6) {
                    throw new RuntimeException("La contrasenya ha de tenir almenys 6 caràcters.");
                }
                if (!usuariModificat.getContrasenya().matches("^(?=.*[A-Za-z])(?=.*\\d).+$")) {
                    throw new RuntimeException("La contrasenya ha de contenir com a mínim una lletra i un número.");
                }
                usuariAntic.setContrasenya(passwordEncoder.encode(usuariModificat.getContrasenya()));
            }

            usuariAntic.setNom(usuariModificat.getNom());
            usuariAntic.setCognoms(usuariModificat.getCognoms());
            usuariAntic.setDataNaixement(usuariModificat.getDataNaixement());
            usuariAntic.setTelefon(usuariModificat.getTelefon());
            usuariAntic.setRol(usuariModificat.getRol());
            usuariAntic.setPoblacio(usuariModificat.getPoblacio());
            usuariAntic.setObservacions(usuariModificat.getObservacions());

            if (usuariModificat.getFoto() != null && usuariModificat.getFoto().length > 0) {
                usuariAntic.setFoto(usuariModificat.getFoto());
            }
            if (usuariModificat.getFoto() != null && usuariModificat.getFoto().length > 1_000_000) { // 1 MB
                throw new RuntimeException("La foto és massa gran. Ha de ser inferior a 1MB.");
            }

            return usuariRepository.save(usuariAntic);
        } else {
            throw new RuntimeException("No s'ha trobat l'usuari amb el correu: " + usuariModificat.getEmail());
        }
    }
}
