package cat.copernic.ymelero.entrebicis.controller.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    private static final Logger log = LoggerFactory.getLogger(ErrorController.class);

    /**
     * Mètode que redirigeix a una pàgina d'error genèrica.
     *
     * @return Vista d'error.
     */
    @GetMapping("/error")
    public String paginaError(Model model) {
        log.warn("S'ha redirigit a la pàgina d'error genèrica");
        model.addAttribute("errorMessage",
                "Error de connexió amb la base de dades o el servidor. Comprova que tot estigui encès.");
        return "error";
    }
}
