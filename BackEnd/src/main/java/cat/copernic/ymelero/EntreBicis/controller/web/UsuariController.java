package cat.copernic.ymelero.entrebicis.controller.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import cat.copernic.ymelero.entrebicis.entity.Usuari;
import cat.copernic.ymelero.entrebicis.logic.web.UsuariLogica;

@Controller
public class UsuariController {

    @Autowired
    private UsuariLogica usuariLogica;

    @GetMapping("/usuaris")
    public String mostrarUsuaris(Model model) {
        List<Usuari> usuaris = usuariLogica.getAllUsuaris();
        model.addAttribute("usuaris", usuaris);
        return "usuaris";
    }
}
