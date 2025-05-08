package cat.copernic.ymelero.entrebicis.logic.web;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.copernic.ymelero.entrebicis.entity.Ruta;
import cat.copernic.ymelero.entrebicis.repository.RutaRepository;

@Service
public class RutaLogica {

    @Autowired
    private RutaRepository rutaRepository;

    public Ruta iniciarRuta(Ruta ruta) {
        if (ruta.getDataCreacio() == null) {
            ruta.setDataCreacio(LocalDate.now());
            System.out.println("Usuari: " + ruta.getUsuari());
            System.out.println("Data: " + ruta.getDataCreacio());
            System.out.println("Estat: " + ruta.getEstat());
        }
        return rutaRepository.save(ruta);
    }
}
