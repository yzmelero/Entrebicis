package cat.copernic.ymelero.entrebicis.controller.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cat.copernic.ymelero.entrebicis.entity.Usuari;
import cat.copernic.ymelero.entrebicis.logic.web.UsuariLogica;

@Controller
@RequestMapping("/usuaris")
public class UsuariController {

    @Autowired
    private UsuariLogica usuariLogica;

    @GetMapping
    public String mostrarUsuaris(Model model) {
        List<Usuari> usuaris = usuariLogica.getAllUsuaris();
        model.addAttribute("usuaris", usuaris);
        return "usuaris";
    }

    @GetMapping("/crear")
    public String crearUsuari(Model model) {
        model.addAttribute("usuari", new Usuari());
        return "usuari-alta";
    }

    @PostMapping("/crear")
    public String guardarNouUsuari(@ModelAttribute Usuari usuari, Model model) {
        try {
            usuariLogica.crearUsuari(usuari);
            return "redirect:/usuaris";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("usuari", usuari);
            return "usuari-alta";
        }
    }
}
