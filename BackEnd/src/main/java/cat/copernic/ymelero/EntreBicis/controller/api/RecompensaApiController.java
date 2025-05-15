package cat.copernic.ymelero.entrebicis.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cat.copernic.ymelero.entrebicis.entity.Recompensa;
import cat.copernic.ymelero.entrebicis.logic.web.RecompensaLogica;

@RestController
@RequestMapping("/api/recompenses")
@CrossOrigin(origins = "*")
public class RecompensaApiController {

    @Autowired
    private RecompensaLogica recompensaLogica;

    @GetMapping("/disponibles")
    public List<Recompensa> getRecompensesDisponibles() {
        return recompensaLogica.getRecompensesDisponibles();
    }

    @GetMapping("/propies")
    public List<Recompensa> getRecompensesPropies(@RequestParam String email) {
        return recompensaLogica.getRecompensesPropies(email);
    }

    @GetMapping
    public Recompensa getRecompensa(@RequestParam Long id) {
        return recompensaLogica.getRecompensa(id);
    }

    @PostMapping("/reservar")
    public ResponseEntity<?> reservarRecompensa(
            @RequestParam Long recompensaId,
            @RequestParam String email,
            @RequestParam Double saldo) {
        try {
            Recompensa recompensa = recompensaLogica.reservarRecompensa(recompensaId, email, saldo);
            return ResponseEntity.ok(recompensa);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
