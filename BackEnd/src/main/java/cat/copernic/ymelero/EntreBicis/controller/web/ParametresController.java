package cat.copernic.ymelero.entrebicis.controller.web;

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

    @Autowired
    private ParametresLogica parametresLogica;

    @GetMapping
    public String mostrarParametres(Model model) {
        ParametresSistema parametres = parametresLogica.getParametres();
        if (parametres == null) {
            parametres = new ParametresSistema();
        }
        model.addAttribute("parametres", parametres);
        return "parametres-modificar";
    }

    @PostMapping
    public String guardarParametres(@ModelAttribute ParametresSistema parametres, Model model) {
        try {
            parametresLogica.guardarParametres(parametres);
            return "redirect:/parametres";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("parametres", parametres);
            return "parametres-modificar";
        }
    }
}