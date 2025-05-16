package cat.copernic.ymelero.entrebicis.controller.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    /**
     * Mètode que redirigeix a la pàgina de login.
     *
     * @param error Paràmetre que indica si hi ha hagut un error en el login.
     * @param model Model per passar dades a la vista.
     * @return Nom de la vista de login.
     */
    @GetMapping("/login")
    public String IniciLogin(@RequestParam(value = "error", required = false) String error, Model model) {
        if ("noAdmin".equals(error)) { // TO-DO Implementar Autentificador personalitzat mes endavant per que funcioni
            log.warn("Intent d'accés sense permisos d'administrador");
            model.addAttribute("errorMessage", "No tens permisos d'administrador.");
        } else if ("true".equals(error)) {
            log.warn("Login fallit: credencials incorrectes");
            model.addAttribute("errorMessage", "Email o contrasenya incorrectes.");
        }
        return "login";
    }

}
