package cat.copernic.ymelero.entrebicis.controller.api;

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

    @Autowired
    private UsuariLogica usuariLogica;

    @GetMapping("/visualitzar/{email}")
    public Usuari getUsuari(@PathVariable String email) {
        return usuariLogica.getUsuari(email);
    }

    @PutMapping("/modificar")
    public ResponseEntity<?> modificarUsuari(@RequestBody Usuari usuariModificat) {
        try {
            Usuari usuariActualitzat = usuariLogica.modificarUsuari(usuariModificat);
            return ResponseEntity.ok(usuariActualitzat);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
