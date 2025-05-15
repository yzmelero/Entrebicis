package cat.copernic.ymelero.entrebicis.controller.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/validar/{idRuta}")
    public String validarRuta(@PathVariable Long idRuta) {
        rutaLogica.validarRuta(idRuta);
        return "redirect:/rutes/consultar/" + idRuta;
    }

    @PostMapping("/invalidar/{idRuta}")
    public String invalidarRuta(@PathVariable Long idRuta, Model model) {
        try {
            rutaLogica.invalidarRuta(idRuta);
            return "redirect:/rutes/consultar/" + idRuta;
        } catch (RuntimeException ex) {
            Ruta ruta = rutaLogica.obtenirRuta(idRuta);
            model.addAttribute("ruta", ruta);
            model.addAttribute("puntGPS", ruta.getPuntGPS());
            model.addAttribute("maxVelocitat", rutaLogica.getParametres().getVelocitatMaxima());
            model.addAttribute("error", ex.getMessage());
            return "ruta-consultar";
        }
    }

    @GetMapping("/historial/{email}")
    public String veureHistorialRutes(@PathVariable String email, Model model) {
        List<Ruta> rutes = rutaLogica.getRutesPerUsuari(email);
        model.addAttribute("rutes", rutes);

        if (!rutes.isEmpty() && rutes.get(0).getUsuari() != null) {
            model.addAttribute("usuariRutes", rutes.get(0).getUsuari());
        }
        return "ruta-llistar";
    }
}