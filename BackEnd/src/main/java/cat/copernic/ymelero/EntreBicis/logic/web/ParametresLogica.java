package cat.copernic.ymelero.entrebicis.logic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.copernic.ymelero.entrebicis.entity.ParametresSistema;
import cat.copernic.ymelero.entrebicis.repository.ParametresSistemaRepository;

/**
 * Classe de lògica de negoci per gestionar els paràmetres del sistema.
 */
@Service
public class ParametresLogica {

    @Autowired
    private ParametresSistemaRepository parametresRepository;

    /**
     * Obté els paràmetres del sistema.
     *
     * @return Els paràmetres del sistema.
     */
    public ParametresSistema getParametres() {
        return parametresRepository.findById(1L).orElse(null);
    }

    /**
     * Desa els paràmetres del sistema.
     *
     * @param parametres Els paràmetres a desar.
     */
    public void guardarParametres(ParametresSistema parametres) {
        parametres.setId(1L); // Sempre 1
        parametresRepository.save(parametres);
    }
}
