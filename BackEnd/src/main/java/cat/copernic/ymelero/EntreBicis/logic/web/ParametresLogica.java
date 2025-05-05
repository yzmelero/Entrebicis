package cat.copernic.ymelero.entrebicis.logic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.copernic.ymelero.entrebicis.entity.ParametresSistema;
import cat.copernic.ymelero.entrebicis.repository.ParametresSistemaRepository;

@Service
public class ParametresLogica {

    @Autowired
    private ParametresSistemaRepository parametresRepository;

    public ParametresSistema getParametres() {
        return parametresRepository.findById(1L).orElse(null);
    }

    public void guardarParametres(ParametresSistema parametres) {
        parametres.setId(1L); // Sempre 1
        parametresRepository.save(parametres);
    }
}
