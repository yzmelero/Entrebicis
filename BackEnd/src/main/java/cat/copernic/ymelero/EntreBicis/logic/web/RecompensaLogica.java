package cat.copernic.ymelero.entrebicis.logic.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.copernic.ymelero.entrebicis.entity.EstatRecompensa;
import cat.copernic.ymelero.entrebicis.entity.Recompensa;
import cat.copernic.ymelero.entrebicis.repository.RecompensaRepository;

@Service
public class RecompensaLogica {

    @Autowired
    private RecompensaRepository recompensaRepository;

    public List<Recompensa> getAllRecompenses() {
        return recompensaRepository.findAll();
    }

    public Recompensa crearRecompensa(Recompensa recompensa) throws RuntimeException {
        if (recompensa.getDescripcio() == null || recompensa.getDescripcio().isEmpty()) {
            throw new RuntimeException("La descripció de la recompensa és obligatòria.");
        }
        if (recompensa.getPunts() == null) {
            throw new RuntimeException("Els punts són obligatoris.");
        }
        if (recompensa.getPunts() <= 0) {
            throw new RuntimeException("Els punts han de ser un valor positiu.");
        }
        if (recompensa.getNomComerç() == null || recompensa.getNomComerç().isEmpty()) {
            throw new RuntimeException("El nom del comerç és obligatori.");
        }
        if (recompensa.getAdreçaComerç() == null || recompensa.getAdreçaComerç().isEmpty()) {
            throw new RuntimeException("L'adreça del comerç és obligatòria.");
        }
        if (recompensa.getEstat() == null) {
            recompensa.setEstat(EstatRecompensa.DISPONIBLE);
        }
        if (recompensa.getEstat() == EstatRecompensa.DISPONIBLE) {
            recompensa.setUsuari(null);
        }
        return recompensaRepository.save(recompensa);
    }

    public Recompensa getRecompensa(Long id) {
        return recompensaRepository.findById(id).orElse(null);
    }
}
