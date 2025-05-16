package cat.copernic.ymelero.entrebicis.controller.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cat.copernic.ymelero.entrebicis.entity.Ruta;
import cat.copernic.ymelero.entrebicis.entity.Usuari;
import cat.copernic.ymelero.entrebicis.logic.web.RutaLogica;

@Controller
@RequestMapping("/rutes")
public class RutaController {

    private static final Logger log = LoggerFactory.getLogger(RutaController.class);

    @Autowired
    private RutaLogica rutaLogica;

    /**
     * Mètode per mostrar el llistat de rutes.
     *
     * @param model Model per passar dades a la vista.
     * @return Nom de la vista a mostrar.
     */
    @GetMapping
    public String llistarRutes(Model model) {
        log.info("Accedint al llistat de totes les rutes");
        List<Ruta> rutes = rutaLogica.llistarTotesLesRutes();
        model.addAttribute("rutes", rutes);
        return "ruta-llistar";
    }

    /**
     * Mètode per mostrar una ruta específica.
     * 
     * @param idRuta ID de la ruta a consultar.
     * @param model  Model per passar dades a la vista.
     * @return Nom de la vista a mostrar.
     */
    @GetMapping("/consultar/{idRuta}")
    public String consultarRuta(@PathVariable Long idRuta, Model model) {
        log.info("Consultant la ruta amb ID: {}", idRuta);
        Ruta ruta = rutaLogica.obtenirRuta(idRuta);

        model.addAttribute("ruta", ruta);
        model.addAttribute("puntGPS", ruta.getPuntGPS());
        model.addAttribute("maxVelocitat", rutaLogica.getParametres().getVelocitatMaxima());
        return "ruta-consultar";
    }

    /**
     * Mètode per validar una ruta.
     * 
     * @param idRuta ID de la ruta a validar.
     * @return Redirecció a la vista de consulta de la ruta.
     */
    @PostMapping("/validar/{idRuta}")
    public String validarRuta(@PathVariable Long idRuta) {
        log.info("Validant la ruta amb ID: {}", idRuta);
        rutaLogica.validarRuta(idRuta);
        return "redirect:/rutes/consultar/" + idRuta;
    }

    /**
     * Mètode per invalidar una ruta.
     * 
     * @param idRuta ID de la ruta a invalidar.
     * @param model  Model per passar dades a la vista.
     * @return Redirecció a la vista de consulta de la ruta o error.
     */
    @PostMapping("/invalidar/{idRuta}")
    public String invalidarRuta(@PathVariable Long idRuta, Model model) {
        try {
            log.info("Intentant invalidar la ruta amb ID: {}", idRuta);
            rutaLogica.invalidarRuta(idRuta);
            return "redirect:/rutes/consultar/" + idRuta;
        } catch (RuntimeException ex) {
            log.error("Error en invalidar la ruta amb ID: {}: {}", idRuta, ex.getMessage(), ex);
            Ruta ruta = rutaLogica.obtenirRuta(idRuta);
            model.addAttribute("ruta", ruta);
            model.addAttribute("puntGPS", ruta.getPuntGPS());
            model.addAttribute("maxVelocitat", rutaLogica.getParametres().getVelocitatMaxima());
            model.addAttribute("error", ex.getMessage());
            return "ruta-consultar";
        }
    }

    /**
     * Mètode per mostrar l'historial de rutes d'un usuari.
     * 
     * @param email L'email de l'usuari.
     * @param model Model per passar dades a la vista.
     * @return Nom de la vista a mostrar.
     */
    @GetMapping("/historial/{email}")
    public String veureHistorialRutes(@PathVariable String email, Model model) {
        log.info("Consultant historial de rutes per a l'usuari: {}", email);
        List<Ruta> rutes = rutaLogica.llistarRutesPerUsuari(email);
        model.addAttribute("rutes", rutes);

        Usuari usuari = rutaLogica.getUsuariPerEmail(email);
        model.addAttribute("usuariRutes", usuari);

        return "ruta-llistar";
    }

}