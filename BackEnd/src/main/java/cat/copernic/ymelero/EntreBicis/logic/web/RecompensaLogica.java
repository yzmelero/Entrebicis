package cat.copernic.ymelero.entrebicis.logic.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.copernic.ymelero.entrebicis.entity.Recompensa;
import cat.copernic.ymelero.entrebicis.repository.RecompensaRepository;

@Service
public class RecompensaLogica {

    @Autowired
    private RecompensaRepository recompensaRepository;

    public List<Recompensa> getAllRecompenses() {
        return recompensaRepository.findAll();
    }
}
