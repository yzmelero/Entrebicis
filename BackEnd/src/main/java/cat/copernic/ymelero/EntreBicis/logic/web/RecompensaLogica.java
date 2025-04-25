package cat.copernic.ymelero.entrebicis.logic.web;

import java.time.LocalDate;
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
        if (recompensa.getNomComerc() == null || recompensa.getNomComerc().isEmpty()) {
            throw new RuntimeException("El nom del comerç és obligatori.");
        }
        if (recompensa.getAdrecaComerc() == null || recompensa.getAdrecaComerc().isEmpty()) {
            throw new RuntimeException("L'adreça del comerç és obligatòria.");
        }
        if (recompensa.getEstat() == null) {
            recompensa.setEstat(EstatRecompensa.DISPONIBLE);
        }
        if (recompensa.getEstat() == EstatRecompensa.DISPONIBLE) {
            recompensa.setUsuari(null);
        }

        if (recompensa.getDataCreacio() == null) {
            recompensa.setDataCreacio(LocalDate.now());
        }
        return recompensaRepository.save(recompensa);
    }

    public Recompensa getRecompensa(Long id) {
        return recompensaRepository.findById(id).orElse(null);
    }

    public void eliminarRecompensa(Long id) {
        Recompensa recompensa = getRecompensa(id);
        if (recompensa != null) {
            if (EstatRecompensa.DISPONIBLE.equals(recompensa.getEstat())) {
                recompensaRepository.deleteById(id);
            } else {
                throw new RuntimeException("Només es poden eliminar recompenses amb l'estat DISPONIBLE.");
            }
        } else {
            throw new RuntimeException("No s'ha trobat la recompensa amb id: " + id);
        }
    }

    public List<Recompensa> getRecompensesDisponibles() {
        return recompensaRepository.findByEstat(EstatRecompensa.DISPONIBLE);
    }

    public List<Recompensa> getRecompensesPropies(String email) {
        return recompensaRepository.findByUsuari_Email(email);
    }
}
