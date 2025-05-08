package cat.copernic.ymelero.entrebicis.logic.web;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.copernic.ymelero.entrebicis.entity.EstatRuta;
import cat.copernic.ymelero.entrebicis.entity.PuntGPS;
import cat.copernic.ymelero.entrebicis.entity.Ruta;
import cat.copernic.ymelero.entrebicis.entity.Usuari;
import cat.copernic.ymelero.entrebicis.repository.PuntsRepository;
import cat.copernic.ymelero.entrebicis.repository.RutaRepository;
import cat.copernic.ymelero.entrebicis.repository.UsuariRepository;

@Service
public class RutaLogica {

    @Autowired
    private RutaRepository rutaRepository;

    @Autowired
    private UsuariRepository usuariRepository;

    @Autowired
    private PuntsRepository puntsRepository;

    public Ruta iniciarRuta(Ruta ruta) {
        if (ruta.getUsuari() == null || ruta.getUsuari().getEmail() == null) {
            throw new RuntimeException("L'usuari o el correu electrònic és nul.");
        }

        Usuari usuari = usuariRepository.findByEmail(ruta.getUsuari().getEmail())
                .orElseThrow(() -> new RuntimeException("Usuari no trobat"));

        ruta.setUsuari(usuari);

        if (ruta.getDataCreacio() == null) {
            ruta.setDataCreacio(LocalDate.now());
        }
        if (ruta.getEstat() == null) {
            ruta.setEstat(EstatRuta.PENDENT);
        }
        return rutaRepository.save(ruta);
    }

    public PuntGPS afegirPuntGPS(Long idRuta, PuntGPS punt) {
        Ruta ruta = rutaRepository.findById(idRuta)
                .orElseThrow(() -> new RuntimeException("Ruta no trobada"));

        punt.setRuta(ruta);
        punt.setMarcaTemps(LocalDateTime.now());
        return puntsRepository.save(punt);
    }

    public Ruta finalitzarRuta(Long idRuta) {
        Ruta ruta = rutaRepository.findById(idRuta)
                .orElseThrow(() -> new RuntimeException("Ruta no trobada"));

        if (ruta.getEstat() != EstatRuta.PENDENT) {
            throw new RuntimeException("Només es poden finalitzar rutes en estat PENDENT.");
        }
        ruta.setEstat(EstatRuta.NOVALIDADA);
        return rutaRepository.save(ruta);
    }

}
