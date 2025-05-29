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

/**
 * Classe de lògica de negoci per gestionar les rutes.
 */
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

    /**
     * Obté una ruta per ID.
     *
     * @param idRuta L'ID de la ruta a obtenir.
     * @return La ruta corresponent a l'ID.
     */
    public Ruta obtenirRuta(Long idRuta) {
        if (idRuta == null || idRuta <= 0) {
            throw new RuntimeException("L'identificador de la ruta és invàlid.");
        }
        return rutaRepository.findById(idRuta)
                .orElseThrow(() -> new RuntimeException("Ruta no trobada amb id: " + idRuta));
    }

    /**
     * Obté totes les rutes disponibles.
     *
     * @return Llista de rutes.
     */
    public List<Ruta> llistarTotesLesRutes() {
        List<Ruta> rutes = rutaRepository.findAllByOrderByDataCreacioDesc();
        if (rutes.isEmpty()) {
            throw new RuntimeException("No hi ha rutes disponibles.");
        }
        return rutes;
    }

    /**
     * Obté els paràmetres del sistema.
     *
     * @return Els paràmetres del sistema.
     */
    public ParametresSistema getParametres() {
        return parametresLogica.getParametres();
    }

    /**
     * Obté les rutes d'un usuari per email.
     *
     * @param email L'email de l'usuari.
     * @return Llista de rutes de l'usuari.
     */
    public List<Ruta> llistarRutesPerUsuari(String email) {
        if (email == null || email.isEmpty()) {
            throw new RuntimeException("El correu electrònic de l'usuari és obligatori.");
        }
        usuariRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuari no trobat"));
        return rutaRepository.findByUsuariEmailOrderByDataCreacioDesc(email);
    }

    /**
     * Inicia una ruta.
     *
     * @param ruta La ruta a iniciar.
     * @return La ruta iniciada.
     */
    public Ruta iniciarRuta(Ruta ruta) {
        if (ruta.getDistancia() != null && ruta.getDistancia() < 0) {
            throw new RuntimeException("La distància no pot ser negativa.");
        }
        if (ruta.getTempsTotal() != null && ruta.getTempsTotal() < 0) {
            throw new RuntimeException("El temps total no pot ser negatiu.");
        }

        if (ruta.getUsuari() == null || ruta.getUsuari().getEmail() == null) {
            throw new RuntimeException("Usuari no trobat amb email: " + ruta.getUsuari().getEmail());
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

    /**
     * Afegeix un punt GPS a una ruta.
     *
     * @param idRuta L'ID de la ruta.
     * @param punt   El punt GPS a afegir.
     * @return El punt GPS afegit.
     */
    public PuntGPS afegirPuntGPS(Long idRuta, PuntGPS punt) {
        if (punt.getLatitud() == null || punt.getLongitud() == null) {
            throw new RuntimeException("Les coordenades del punt GPS són obligatòries.");
        }
        Ruta ruta = rutaRepository.findById(idRuta)
                .orElseThrow(() -> new RuntimeException("Ruta no trobada"));

        punt.setRuta(ruta);
        if (punt.getMarcaTemps() == null) {
            punt.setMarcaTemps(LocalDateTime.now());
        }

        return puntsRepository.save(punt);
    }

    /**
     * Finalitza una ruta.
     *
     * @param idRuta L'ID de la ruta a finalitzar.
     * @return La ruta finalitzada.
     */
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

            if (segons > 0 && distancia >= 1) {
                double velocitat = (distancia / segons) * 3.6; // m/s a km/h
                if (velocitat > 0.5 && velocitat <= 120) {
                    if (velocitat > velocitatMax)
                        velocitatMax = velocitat;
                }
            }
        }

        long segonsTotals = Duration.between(primer.getMarcaTemps(), ultim.getMarcaTemps()).getSeconds();
        double horesTotals = segonsTotals / 3600.0;
        double velocitatMitjana = horesTotals > 0 ? (distanciaTotal / 1000.0) / horesTotals : 0;

        ParametresSistema parametres = parametresLogica.getParametres();
        double km = distanciaTotal / 1000.0;
        double saldoObtingut = km * parametres.getConversioQuilometreSaldo();
        if (horesTotals <= 0 || distanciaTotal <= 0) {
            throw new RuntimeException("No es pot finalitzar la ruta perquè les dades són invàlides.");
        }
        if (distanciaTotal < 2.0) {
            puntsRepository.deleteAll(punts);
            rutaRepository.delete(ruta);
            throw new RuntimeException("Ruta descartada: no s'ha recorregut cap distància significativa.");
        }
        if (saldoObtingut < 0) {
            throw new RuntimeException("El saldo obtingut no pot ser negatiu.");
        }

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

    /**
     * Calcula la distància entre dos punts geogràfics utilitzant la fórmula de
     * Haversine.
     * 
     * Aquesta fórmula té en compte la curvatura de la Terra per retornar una
     * distància
     * més precisa en metres entre dos punts de latitud i longitud.
     *
     * @param lat1 Latitud del primer punt (en graus decimals).
     * @param lon1 Longitud del primer punt (en graus decimals).
     * @param lat2 Latitud del segon punt (en graus decimals).
     * @param lon2 Longitud del segon punt (en graus decimals).
     * @return La distància entre els dos punts en metres.
     */
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

    /**
     * Valida una ruta.
     *
     * @param idRuta L'ID de la ruta a validar.
     */
    public void validarRuta(Long idRuta) {
        Ruta ruta = obtenirRuta(idRuta);
        if (ruta.getEstat() != EstatRuta.NOVALIDADA) {
            throw new RuntimeException("Només es poden validar rutes amb estat NO VALIDADA.");
        }
        ruta.setValidada(true);
        ruta.setEstat(EstatRuta.VALIDADA);

        Usuari usuari = ruta.getUsuari();
        usuari.setSaldo(usuari.getSaldo() + ruta.getSaldoObtingut());
        usuariRepository.save(usuari);
        rutaRepository.save(ruta);
    }

    /**
     * Invalidar una ruta.
     *
     * @param idRuta L'ID de la ruta a invalidar.
     */
    public void invalidarRuta(Long idRuta) {
        Ruta ruta = obtenirRuta(idRuta);

        if (ruta.getEstat() != EstatRuta.VALIDADA) {
            throw new RuntimeException("Només es poden invalidar rutes amb estat VALIDADA.");
        }

        Usuari usuari = ruta.getUsuari();

        if (usuari.getSaldo() == null || usuari.getSaldo() < ruta.getSaldoObtingut()) {
            throw new RuntimeException("El saldo de l'usuari és insuficient per invalidar la ruta.");
        }

        usuari.setSaldo(usuari.getSaldo() - ruta.getSaldoObtingut());
        ruta.setValidada(false);
        ruta.setEstat(EstatRuta.NOVALIDADA);
        usuariRepository.save(usuari);
        rutaRepository.save(ruta);
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