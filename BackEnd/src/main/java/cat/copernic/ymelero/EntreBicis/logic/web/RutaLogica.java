package cat.copernic.ymelero.entrebicis.logic.web;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.copernic.ymelero.entrebicis.entity.EstatRuta;
import cat.copernic.ymelero.entrebicis.entity.ParametresSistema;
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

    @Autowired
    private ParametresLogica parametresLogica;

    public Ruta obtenirRuta(Long idRuta) {
        return rutaRepository.findById(idRuta)
                .orElseThrow(() -> new RuntimeException("Ruta no trobada"));
    }

    public List<Ruta> llistarRutesPerUsuari(String email) {
        usuariRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuari no trobat"));
        return rutaRepository.findByUsuariEmailOrderByDataCreacioDesc(email);
    }

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
            throw new RuntimeException("La ruta no està en curs.");
        }

        List<PuntGPS> punts = puntsRepository.findByRutaIdOrderByMarcaTempsAsc(idRuta);

        if (punts.size() < 2) {
            throw new RuntimeException("No hi ha prou punts GPS per calcular la ruta.");
        }

        double distanciaTotal = 0.0;
        double velocitatMax = 0.0;

        PuntGPS primer = punts.get(0);
        PuntGPS ultim = punts.get(punts.size() - 1);

        for (int i = 1; i < punts.size(); i++) {
            PuntGPS anterior = punts.get(i - 1);
            PuntGPS actual = punts.get(i);

            double distancia = calcularDistanciaHaversine(
                    anterior.getLatitud(), anterior.getLongitud(),
                    actual.getLatitud(), actual.getLongitud());

            distanciaTotal += distancia;

            Duration duracio = Duration.between(anterior.getMarcaTemps(), actual.getMarcaTemps());
            long segons = duracio.getSeconds();

            if (segons > 0) {
                double velocitat = (distancia / segons) * 3.6; // m/s a km/h
                if (velocitat > velocitatMax)
                    velocitatMax = velocitat;
            }
        }

        long segonsTotals = Duration.between(primer.getMarcaTemps(), ultim.getMarcaTemps()).getSeconds();
        double horesTotals = segonsTotals / 3600.0;
        double velocitatMitjana = horesTotals > 0 ? (distanciaTotal / 1000.0) / horesTotals : 0;

        ParametresSistema parametres = parametresLogica.getParametres();
        double km = distanciaTotal / 1000.0;
        double saldoObtingut = km * parametres.getConversioQuilometreSaldo();

        System.out.printf("Distància: %.2f m | Temps: %.2f h | VMax: %.2f km/h | VMitjana: %.2f km/h%n",
                distanciaTotal, horesTotals, velocitatMax, velocitatMitjana);

        ruta.setSaldoObtingut(saldoObtingut);
        ruta.setDistancia(distanciaTotal);
        ruta.setTempsTotal(horesTotals);
        ruta.setVelocitatMaxima(velocitatMax);
        ruta.setVelocitatMitjana(velocitatMitjana);
        ruta.setValidada(false);
        ruta.setEstat(EstatRuta.NOVALIDADA);

        return rutaRepository.save(ruta);
    }

    // Calcular la distància amb el mètode Haversine per que sigui més precisa
    private double calcularDistanciaHaversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // Radi de la Terra en metres
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}