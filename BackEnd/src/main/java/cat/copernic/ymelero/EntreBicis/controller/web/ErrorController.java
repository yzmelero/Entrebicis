package cat.copernic.ymelero.entrebicis.controller.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    private static final Logger log = LoggerFactory.getLogger(ErrorController.class);

    @GetMapping("/error")
    public String paginaError() {
        log.warn("S'ha redirigit a la pàgina d'error genèrica");
        return "error";
    }
}