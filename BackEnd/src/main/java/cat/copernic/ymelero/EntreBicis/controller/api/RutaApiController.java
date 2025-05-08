package cat.copernic.ymelero.entrebicis.controller.api;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.copernic.ymelero.entrebicis.entity.EstatRuta;
import cat.copernic.ymelero.entrebicis.entity.Ruta;
import cat.copernic.ymelero.entrebicis.entity.Usuari;
import cat.copernic.ymelero.entrebicis.logic.web.RutaLogica;
import cat.copernic.ymelero.entrebicis.repository.UsuariRepository;

@RestController
@RequestMapping("/api/ruta")
public class RutaApiController {

    @Autowired
    private RutaLogica rutaLogica;

    @Autowired
    private UsuariRepository usuariRepository;

    @PostMapping("/iniciar")
    public ResponseEntity<?> iniciarRuta(@RequestBody Ruta ruta) {
        try {
            if (ruta.getUsuari() == null || ruta.getUsuari().getEmail() == null) {
                return ResponseEntity.badRequest().body("L'usuari o el correu electrònic és nul.");
            }

            Usuari usuari = usuariRepository.findByEmail(ruta.getUsuari().getEmail())
                    .orElseThrow(() -> new RuntimeException("Usuari no trobat"));

            ruta.setUsuari(usuari);
            ruta.setDataCreacio(LocalDate.now());
            ruta.setEstat(EstatRuta.PENDENT);

            Ruta novaRuta = rutaLogica.iniciarRuta(ruta);

            return ResponseEntity.ok(novaRuta);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error al iniciar ruta: " + e.getMessage());
        }
    }
}
