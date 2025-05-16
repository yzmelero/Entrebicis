package cat.copernic.ymelero.entrebicis.controller.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.copernic.ymelero.entrebicis.entity.PuntGPS;
import cat.copernic.ymelero.entrebicis.entity.Ruta;
import cat.copernic.ymelero.entrebicis.logic.web.RutaLogica;

@RestController
@RequestMapping("/api/ruta")
public class RutaApiController {

    private static final Logger log = LoggerFactory.getLogger(RutaApiController.class);

    @Autowired
    private RutaLogica rutaLogica;

    @GetMapping("/{idRuta}")
    public ResponseEntity<?> obtenirRuta(@PathVariable Long idRuta) {
        try {
            log.info("Consultant la ruta amb ID: {}", idRuta);
            Ruta ruta = rutaLogica.obtenirRuta(idRuta);
            return ResponseEntity.ok(ruta);
        } catch (Exception e) {
            log.error("Error consultant la ruta: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuari/{email}")
    public ResponseEntity<?> llistarRutesPerUsuari(@PathVariable String email) {
        try {
            log.info("Consultant rutes per l'usuari: {}", email);
            List<Ruta> rutes = rutaLogica.llistarRutesPerUsuari(email);
            return ResponseEntity.ok(rutes);
        } catch (Exception e) {
            log.error("Error consultant rutes per l'usuari: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/iniciar")
    public ResponseEntity<?> iniciarRuta(@RequestBody Ruta ruta) {
        try {
            log.info("Iniciant ruta: {}", ruta);
            Ruta novaRuta = rutaLogica.iniciarRuta(ruta);
            return ResponseEntity.ok(novaRuta);
        } catch (Exception e) {
            log.error("Error iniciant ruta: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error al iniciar ruta: " + e.getMessage());
        }
    }

    @PostMapping("/{idRuta}/puntgps")
    public ResponseEntity<?> afegirPuntGPS(@PathVariable Long idRuta, @RequestBody PuntGPS punt) {
        try {
            PuntGPS nouPunt = rutaLogica.afegirPuntGPS(idRuta, punt);
            return ResponseEntity.ok(nouPunt);
        } catch (Exception e) {
            log.error("Error afegint punt GPS: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/{idRuta}/finalitzar")
    public ResponseEntity<?> finalitzarRuta(@PathVariable Long idRuta) {
        try {
            log.info("Finalitzant ruta amb ID: {}", idRuta);
            Ruta rutaFinalitzada = rutaLogica.finalitzarRuta(idRuta);
            return ResponseEntity.ok(rutaFinalitzada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
