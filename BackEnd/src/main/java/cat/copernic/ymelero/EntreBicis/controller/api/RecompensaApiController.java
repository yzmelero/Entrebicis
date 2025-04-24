package cat.copernic.ymelero.entrebicis.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
