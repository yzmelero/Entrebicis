package cat.copernic.ymelero.entrebicis.controller.api;

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

    @Autowired
    private RutaLogica rutaLogica;

    @GetMapping("/{idRuta}")
    public ResponseEntity<?> obtenirRuta(@PathVariable Long idRuta) {
        try {
            Ruta ruta = rutaLogica.obtenirRuta(idRuta);
            return ResponseEntity.ok(ruta);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/iniciar")
    public ResponseEntity<?> iniciarRuta(@RequestBody Ruta ruta) {
        try {
            Ruta novaRuta = rutaLogica.iniciarRuta(ruta);
            return ResponseEntity.ok(novaRuta);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al iniciar ruta: " + e.getMessage());
        }
    }

    @PostMapping("/{idRuta}/puntgps")
    public ResponseEntity<?> afegirPuntGPS(@PathVariable Long idRuta, @RequestBody PuntGPS punt) {
        try {
            PuntGPS nouPunt = rutaLogica.afegirPuntGPS(idRuta, punt);
            return ResponseEntity.ok(nouPunt);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/{idRuta}/finalitzar")
    public ResponseEntity<?> finalitzarRuta(@PathVariable Long idRuta) {
        try {
            Ruta rutaFinalitzada = rutaLogica.finalitzarRuta(idRuta);
            return ResponseEntity.ok(rutaFinalitzada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
