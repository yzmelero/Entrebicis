package cat.copernic.ymelero.entrebicis.controller.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/logout")
public class LogoutController {

    private static final Logger log = LoggerFactory.getLogger(LogoutController.class);

    /**
     * Mètode que gestiona la sortida de l'usuari del sistema.
     *
     * @param request  La petició HTTP.
     * @param response La resposta HTTP.
     * @return Redirigeix a la pàgina de login amb un paràmetre d'success.
     */
    @GetMapping
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
            log.info("Sessió finalitzada correctament per a l'usuari: {}", auth.getName());
        }
        return "redirect:/login?logout=true";
    }
}
