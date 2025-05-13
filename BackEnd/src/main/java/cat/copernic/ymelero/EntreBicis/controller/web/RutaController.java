package cat.copernic.ymelero.entrebicis.controller.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cat.copernic.ymelero.entrebicis.entity.Ruta;
import cat.copernic.ymelero.entrebicis.logic.web.RutaLogica;

@Controller
@RequestMapping("/rutes")
public class RutaController {

    @Autowired
    private RutaLogica rutaLogica;

    @GetMapping
    public String llistarRutes(Model model) {
        List<Ruta> rutes = rutaLogica.llistarTotesLesRutes();
        model.addAttribute("rutes", rutes);
        return "ruta-llistar";
    }

    @GetMapping("/consultar/{idRuta}")
    public String consultarRuta(@PathVariable Long idRuta, Model model) {
        Ruta ruta = rutaLogica.obtenirRuta(idRuta);

        model.addAttribute("ruta", ruta);
        model.addAttribute("puntGPS", ruta.getPuntGPS());
        model.addAttribute("maxVelocitat", rutaLogica.getParametres().getVelocitatMaxima());
        return "ruta-consultar";
    }
}