package cat.copernic.ymelero.entrebicis.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.copernic.ymelero.entrebicis.entity.ParametresSistema;
import cat.copernic.ymelero.entrebicis.logic.web.ParametresLogica;

@RestController
@RequestMapping("/api/parametres")
public class ParametresApiController {

    private static final Logger log = LoggerFactory.getLogger(ParametresApiController.class);

    @Autowired
    private ParametresLogica parametresLogica;

    @GetMapping
    public ResponseEntity<ParametresSistema> obtenirParametres() {
        ParametresSistema parametres = parametresLogica.getParametres();
        if (parametres != null) {
            log.info("Consultant paràmetres del sistema");
            return ResponseEntity.ok(parametres);
        } else {
            log.warn("No hi ha paràmetres definits.");
            return ResponseEntity.notFound().build();
        }
    }
}
