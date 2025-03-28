package cat.copernic.ymelero.entrebicis.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String IniciLogin(@RequestParam(value = "error", required = false) String error, Model model) {
        if ("noAdmin".equals(error)) { // TO-DO Implementar Autentificador personalitzat mes endavant per que funcioni
            model.addAttribute("errorMessage", "No tens permisos d'administrador.");
        } else if ("true".equals(error)) {
            model.addAttribute("errorMessage", "Email o contrasenya incorrectes.");
        }
        return "login";
    }

}
