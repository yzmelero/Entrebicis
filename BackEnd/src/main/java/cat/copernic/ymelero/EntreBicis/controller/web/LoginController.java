package cat.copernic.ymelero.entrebicis.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String IniciLogin() {
        return "login";
    }

}
