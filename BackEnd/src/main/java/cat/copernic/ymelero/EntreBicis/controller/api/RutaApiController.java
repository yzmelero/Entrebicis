package cat.copernic.ymelero.entrebicis.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import cat.copernic.ymelero.entrebicis.entity.Ruta;
import cat.copernic.ymelero.entrebicis.logic.web.RutaLogica;

@Controller
@RequestMapping("/api/ruta")
public class RutaApiController {

    @Autowired
    private RutaLogica rutaLogica;

    @PostMapping("/iniciar")
    public ResponseEntity<Ruta> iniciarRuta(@RequestBody Ruta ruta) {
        try {
            Ruta novaRuta = rutaLogica.iniciarRuta(ruta);
            return ResponseEntity.ok(novaRuta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
