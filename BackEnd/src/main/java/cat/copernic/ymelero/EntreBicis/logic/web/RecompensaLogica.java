package cat.copernic.ymelero.entrebicis.logic.web;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.copernic.ymelero.entrebicis.entity.EstatRecompensa;
import cat.copernic.ymelero.entrebicis.entity.Recompensa;
import cat.copernic.ymelero.entrebicis.entity.Usuari;
import cat.copernic.ymelero.entrebicis.repository.RecompensaRepository;
import cat.copernic.ymelero.entrebicis.repository.UsuariRepository;

/**
 * Classe de lògica de negoci per gestionar les recompenses.
 */
@Service
public class RecompensaLogica {

    @Autowired
    private RecompensaRepository recompensaRepository;

    @Autowired
    private UsuariRepository usuariRepository;

    /**
     * Obté totes les recompenses.
     *
     * @return Llista de recompenses.
     */
    public List<Recompensa> getAllRecompenses() {
        return recompensaRepository.findAll();
    }

    /**
     * Crea una nova recompensa.
     *
     * @param recompensa La recompensa a crear.
     * @return La recompensa creada.
     * @throws RuntimeException Si hi ha un error en la creació de la recompensa.
     */
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

    /**
     * Actualitza una recompensa existent.
     *
     * @param recompensa La recompensa a actualitzar.
     * @return La recompensa actualitzada.
     */
    public Recompensa getRecompensa(Long id) {
        return recompensaRepository.findById(id).orElse(null);
    }

    /**
     * Actualitza una recompensa existent.
     *
     * @param recompensa La recompensa a actualitzar.
     * @return La recompensa actualitzada.
     */
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

    /**
     * Obté les recompenses disponibles.
     *
     * @return Llista de recompenses disponibles.
     */
    public List<Recompensa> getRecompensesDisponibles() {
        return recompensaRepository.findByEstat(EstatRecompensa.DISPONIBLE);
    }

    /**
     * Obté les recompenses d'un usuari.
     *
     * @param email L'email de l'usuari.
     * @return Llista de recompenses de l'usuari.
     */
    public List<Recompensa> getRecompensesPropies(String email) {
        return recompensaRepository.findByUsuari_Email(email);
    }

    /**
     * Reserva una recompensa per a un usuari.
     *
     * @param recompensaId L'ID de la recompensa.
     * @param emailUsuari  L'email de l'usuari.
     * @param saldoUsuari  El saldo de l'usuari.
     * @return La recompensa reservada.
     */
    public Recompensa reservarRecompensa(Long recompensaId, String emailUsuari, Double saldoUsuari) {
        Recompensa recompensa = getRecompensa(recompensaId);

        if (recompensa == null) {
            throw new RuntimeException("No s'ha trobat la recompensa.");
        }

        if (recompensa.getEstat() != EstatRecompensa.DISPONIBLE) {
            throw new RuntimeException("La recompensa no està disponible.");
        }

        if (recompensa.getPunts() > saldoUsuari) {
            throw new RuntimeException("Saldo insuficient.");
        }

        List<Recompensa> recompensesUsuari = recompensaRepository.findByUsuari_Email(emailUsuari);

        for (Recompensa r : recompensesUsuari) {
            if (r.getEstat() == EstatRecompensa.RESERVADA) {
                throw new RuntimeException("Ja tens una recompensa reservada pendent de validar.");
            } else if (r.getEstat() == EstatRecompensa.ASSIGNADA) {
                throw new RuntimeException("Ja tens una recompensa assignada pendent de recollir.");
            }
        }
        Usuari usuari = usuariRepository.findByEmail(emailUsuari)
                .orElseThrow(() -> new RuntimeException("No s'ha trobat l'usuari."));

        recompensa.setEstat(EstatRecompensa.RESERVADA);
        recompensa.setDataReserva(LocalDate.now());
        recompensa.setUsuari(usuari);

        return recompensaRepository.save(recompensa);
    }

    /**
     * Assigna una recompensa a un usuari.
     *
     * @param recompensaId L'ID de la recompensa.
     */
    public void assignarRecompensa(Long recompensaId) {
        Recompensa recompensa = recompensaRepository.findById(recompensaId)
                .orElseThrow(() -> new IllegalArgumentException("Recompensa no trobada"));

        if (recompensa.getEstat() != EstatRecompensa.RESERVADA)
            throw new IllegalStateException("Només es poden assignar recompenses reservades");

        Usuari usuari = recompensa.getUsuari();
        if (usuari == null)
            throw new IllegalStateException("La recompensa no té cap usuari reservat");

        if (usuari.getSaldo() < recompensa.getPunts())
            throw new IllegalStateException("El saldo de l'usuari és insuficient");

        usuari.setSaldo(usuari.getSaldo() - recompensa.getPunts());
        recompensa.setEstat(EstatRecompensa.ASSIGNADA);
        recompensa.setDataAssignacio(LocalDate.now());

        usuariRepository.save(usuari);
        recompensaRepository.save(recompensa);
    }

    /**
     * Recull una recompensa assignada.
     *
     * @param recompensaId L'ID de la recompensa.
     * @return La recompensa recollida.
     */
    public Recompensa recollirRecompensa(Long recompensaId) {
        Recompensa recompensa = recompensaRepository.findById(recompensaId)
                .orElseThrow(() -> new RuntimeException("Recompensa no trobada"));
        if (recompensa.getEstat() != EstatRecompensa.ASSIGNADA) {
            throw new RuntimeException("Només es poden recollir recompenses assignades");
        }
        recompensa.setEstat(EstatRecompensa.RECOLLIDA);
        recompensa.setDataRecollida(LocalDate.now());
        return recompensaRepository.save(recompensa);
    }

    /**
     * Obté un usuari per email.
     *
     * @param email L'email de l'usuari.
     * @return L'usuari corresponent a l'email.
     */
    public Usuari getUsuariPerEmail(String email) {
        return usuariRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuari no trobat"));
    }

}
