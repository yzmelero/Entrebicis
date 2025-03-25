package cat.copernic.ymelero.entrebicis.logic.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.copernic.ymelero.entrebicis.entity.Usuari;
import cat.copernic.ymelero.entrebicis.repository.UsuariRepository;

@Service
public class UsuariLogica {

    @Autowired
    private UsuariRepository usuariRepository;

    public List<Usuari> getAllUsuaris() {
        return usuariRepository.findAll();
    }
}
