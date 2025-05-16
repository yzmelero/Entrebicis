package cat.copernic.ymelero.entrebicis.controller.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cat.copernic.ymelero.entrebicis.entity.ParametresSistema;
import cat.copernic.ymelero.entrebicis.logic.web.ParametresLogica;

@Controller
@RequestMapping("/parametres")
public class ParametresController {

    private static final Logger log = LoggerFactory.getLogger(ParametresController.class);

    @Autowired
    private ParametresLogica parametresLogica;

    @GetMapping
    public String mostrarParametres(Model model) {
        log.info("Consultant paràmetres del sistema");
        ParametresSistema parametres = parametresLogica.getParametres();
        if (parametres == null) {
            log.warn("No hi ha paràmetres definits.");
            parametres = new ParametresSistema();
        }
        model.addAttribute("parametres", parametres);
        return "parametres-modificar";
    }

    @PostMapping
    public String guardarParametres(@ModelAttribute ParametresSistema parametres, Model model) {
        try {
            log.info("Desant paràmetres del sistema");
            parametresLogica.guardarParametres(parametres);
            return "redirect:/parametres";
        } catch (Exception e) {
            log.error("Error en desar els paràmetres", e);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("parametres", parametres);
            return "parametres-modificar";
        }
    }
}