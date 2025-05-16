package cat.copernic.ymelero.entrebicis.controller.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cat.copernic.ymelero.entrebicis.entity.Recompensa;
import cat.copernic.ymelero.entrebicis.logic.web.RecompensaLogica;

@RestController
@RequestMapping("/api/recompenses")
@CrossOrigin(origins = "*")
public class RecompensaApiController {

    private static final Logger log = LoggerFactory.getLogger(RecompensaApiController.class);

    @Autowired
    private RecompensaLogica recompensaLogica;

    @GetMapping("/disponibles")
    public List<Recompensa> getRecompensesDisponibles() {
        log.info("Consultant recompenses disponibles");
        return recompensaLogica.getRecompensesDisponibles();
    }

    @GetMapping("/propies")
    public List<Recompensa> getRecompensesPropies(@RequestParam String email) {
        log.info("Consultant recompenses de l'usuari: {}", email);
        return recompensaLogica.getRecompensesPropies(email);
    }

    @GetMapping
    public Recompensa getRecompensa(@RequestParam Long id) {
        log.info("Consultant recompensa amb ID: {}", id);
        return recompensaLogica.getRecompensa(id);
    }

    @PostMapping("/reservar")
    public ResponseEntity<?> reservarRecompensa(
            @RequestParam Long id,
            @RequestParam String email,
            @RequestParam Double saldo) {
        try {
            log.info("Reservant recompensa amb ID: {} per l'usuari: {}", id, email);
            Recompensa recompensa = recompensaLogica.reservarRecompensa(id, email, saldo);
            return ResponseEntity.ok(recompensa);
        } catch (RuntimeException e) {
            log.error("Error reservant recompensa: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/recollir")
    public ResponseEntity<?> recollirRecompensa(@RequestParam Long id) {
        try {
            log.info("Recollint recompensa amb ID: {}", id);
            Recompensa recompensa = recompensaLogica.recollirRecompensa(id);
            return ResponseEntity.ok(recompensa);
        } catch (RuntimeException e) {
            log.error("Error recollint recompensa: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
