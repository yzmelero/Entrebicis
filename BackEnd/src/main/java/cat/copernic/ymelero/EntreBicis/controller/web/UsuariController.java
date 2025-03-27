package cat.copernic.ymelero.entrebicis.controller.web;

import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cat.copernic.ymelero.entrebicis.entity.Usuari;
import cat.copernic.ymelero.entrebicis.logic.web.UsuariLogica;

@Controller
@RequestMapping("/usuaris")
public class UsuariController {

    @Autowired
    private UsuariLogica usuariLogica;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String mostrarUsuaris(Model model) {
        List<Usuari> usuaris = usuariLogica.getAllUsuaris();
        Map<String, String> imatgesBase64 = new HashMap<>();

        for (Usuari usuari : usuaris) {
            if (usuari.getFoto() != null) {
                String imatgeBase64 = Base64.getEncoder().encodeToString(usuari.getFoto());
                imatgesBase64.put(usuari.getEmail(), imatgeBase64);
            }
        }

        model.addAttribute("usuaris", usuaris);
        model.addAttribute("imatgesBase64", imatgesBase64);
        return "usuaris";
    }

    @GetMapping("/crear")
    public String crearUsuari(Model model) {
        model.addAttribute("usuari", new Usuari());
        return "usuari-alta";
    }

    @PostMapping("/crear")
    public String guardarNouUsuari(@ModelAttribute Usuari usuari,
            @RequestParam(value = "fotoFile", required = false) MultipartFile fotoFile,
            Model model) {
        try {
            usuari.setContrasenya(passwordEncoder.encode(usuari.getContrasenya()));

            if (fotoFile != null && !fotoFile.isEmpty()) {
                usuari.setFoto(fotoFile.getBytes());
            } else {
                ClassPathResource iconUser = new ClassPathResource("static/img/iconUser.png");
                byte[] defaultImage = Files.readAllBytes(iconUser.getFile().toPath());
                usuari.setFoto(defaultImage);
            }
            usuariLogica.crearUsuari(usuari);
            return "redirect:/usuaris";
        } catch (Exception e) {
            model.addAttribute("error", "Error en guardar l'usuari: " + e.getMessage());
            model.addAttribute("usuari", usuari);
            return "usuari-alta";
        }
    }
}
