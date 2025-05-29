package cat.copernic.ymelero.entrebicis.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.copernic.ymelero.entrebicis.entity.Usuari;
import cat.copernic.ymelero.entrebicis.logic.web.UsuariLogica;

@RestController
@RequestMapping("/api/usuari")
@CrossOrigin(origins = "*")
public class UsuariApiController {

    private static final Logger log = LoggerFactory.getLogger(UsuariApiController.class);

    @Autowired
    private UsuariLogica usuariLogica;

    /**
     * Mètode per obtenir un usuari per email.
     *
     * @param email Correu electrònic de l'usuari a consultar.
     * @return Usuari amb el correu electrònic especificat.
     */
    @GetMapping("/visualitzar/{email}")
    public ResponseEntity<?> getUsuari(@PathVariable String email) {
        Usuari usuari = usuariLogica.getUsuari(email);
        if (usuari != null) {
            log.info("Consultant l'usuari amb email: {}", email);
            return ResponseEntity.ok(usuari);
        } else {
            log.warn("No s'ha trobat l'usuari amb email: {}", email);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Mètode per modificar un usuari.
     *
     * @param usuariModificat Usuari amb les dades modificades.
     * @return Usuari actualitzat.
     */
    @PutMapping("/modificar")
    public ResponseEntity<?> modificarUsuari(@RequestBody Usuari usuariModificat) {
        try {
            log.info("Modificant l'usuari: {}", usuariModificat);
            Usuari usuariActualitzat = usuariLogica.modificarUsuari(usuariModificat);
            return ResponseEntity.ok(usuariActualitzat);
        } catch (RuntimeException e) {
            log.error("Error en modificar l'usuari: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
