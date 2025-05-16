package cat.copernic.ymelero.entrebicis.controller.web;

import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cat.copernic.ymelero.entrebicis.entity.Usuari;
import cat.copernic.ymelero.entrebicis.logic.web.UsuariLogica;

@Controller
@RequestMapping("/usuaris")
public class UsuariController {

    private static final Logger log = LoggerFactory.getLogger(UsuariController.class);

    @Autowired
    private UsuariLogica usuariLogica;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Mètode per mostrar el llistat d'usuaris.
     *
     * @param model Model per passar dades a la vista.
     * @return Nom de la vista a mostrar.
     */
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
        log.info("Accedint al llistat de tots els usuaris");
        model.addAttribute("usuaris", usuaris);
        model.addAttribute("imatgesBase64", imatgesBase64);
        return "usuaris";
    }

    /**
     * Mètode per mostrar el formulari de creació d'un nou usuari.
     *
     * @param model Model per passar dades a la vista.
     * @return Nom de la vista a mostrar.
     */
    @GetMapping("/crear")
    public String crearUsuari(Model model) {
        log.info("Creant nou usuari");
        model.addAttribute("usuari", new Usuari());
        return "usuari-alta";
    }

    /**
     * Mètode per guardar un nou usuari.
     *
     * @param usuari               Usuari a guardar.
     * @param fotoFile             Fitxer de la foto de l'usuari.
     * @param confirmarContrasenya Contrasenya per confirmar.
     * @param model                Model per passar dades a la vista.
     * @return Redirecció a la vista dels usuaris o error en cas de fallida.
     */
    @PostMapping("/crear")
    public String guardarNouUsuari(@ModelAttribute Usuari usuari,
            @RequestParam(value = "fotoFile", required = false) MultipartFile fotoFile,
            @RequestParam(value = "confirmarContrasenya", required = false) String confirmarContrasenya,
            Model model) {
        try {
            log.info("Desant nou usuari: {}", usuari.getEmail());
            if (!usuari.getContrasenya().equals(confirmarContrasenya)) {
                throw new RuntimeException("Les contrasenyes no coincideixen.");
            } else {
                usuari.setContrasenya(passwordEncoder.encode(usuari.getContrasenya()));
            }

            if (fotoFile != null && !fotoFile.isEmpty()) {
                if (fotoFile.getSize() > 1_000_000) { // 1MB
                    throw new RuntimeException("La imatge és massa gran.");
                }
                usuari.setFoto(fotoFile.getBytes());
            } else {
                ClassPathResource iconUser = new ClassPathResource("static/img/iconUser.png");
                byte[] defaultImage = Files.readAllBytes(iconUser.getFile().toPath());
                usuari.setFoto(defaultImage);
            }
            usuariLogica.crearUsuari(usuari);
            return "redirect:/usuaris";
        } catch (Exception e) {
            log.error("Error en desar l'usuari", e);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("usuari", usuari);
            return "usuari-alta";
        }
    }

    /**
     * Mètode per mostrar un usuari concret.
     *
     * @param email L'email de l'usuari a mostrar.
     * @param model Model per passar dades a la vista.
     * @return Nom de la vista a mostrar.
     */
    @GetMapping("/consulta/{email}")
    public String mostrarUsuari(@PathVariable String email, Model model) {

        Usuari usuari = usuariLogica.getUsuari(email);
        if (usuari == null) {
            log.warn("No s'ha trobat l'usuari amb correu: {}", email);
            model.addAttribute("error", "No s'ha trobat usuari amb correu: " + email);
            return "redirect:/usuaris";
        }
        if (usuari.getFoto() != null) {
            String imatgeBase64 = Base64.getEncoder().encodeToString(usuari.getFoto());
            model.addAttribute("imatgeBase64", imatgeBase64);
        } else {
            model.addAttribute("imatgeBase64", null);
        }
        log.info("Consultant l'usuari: {}", email);
        model.addAttribute("usuari", usuari);

        return "usuari-consultar";
    }

    /**
     * Mètode per modificar un usuari.
     * 
     * @param email L'email de l'usuari a modificar.
     * @param model Model per passar dades a la vista.
     * @return Nom de la vista a mostrar.
     */
    @GetMapping("/modificar/{email}")
    public String modificarUsuari(@PathVariable String email, Model model) {
        Usuari usuari = usuariLogica.getUsuari(email);
        if (usuari == null) {
            log.warn("No s'ha trobat l'usuari: {}", email);
            model.addAttribute("error", "No s'ha trobat usuari amb correu: " + email);
            return "redirect:/usuaris";
        }
        if (usuari.getFoto() != null) {
            String imatgeBase64 = Base64.getEncoder().encodeToString(usuari.getFoto());
            model.addAttribute("imatgeBase64", imatgeBase64);
        } else {
            model.addAttribute("imatgeBase64", null);
        }
        model.addAttribute("usuari", usuari);

        return "usuari-modificar";
    }

    /**
     * Mètode per guardar la modificació d'un usuari.
     *
     * @param usuari               Usuari a modificar.
     * @param fotoFile             Fitxer de la foto de l'usuari.
     * @param confirmarContrasenya Contrasenya per confirmar.
     * @param model                Model per passar dades a la vista.
     * @return Redirecció a la vista de consulta de l'usuari o error en cas de
     *         fallida.
     */
    @PostMapping("/modificar")
    public String guardarModificacioUsuari(@ModelAttribute Usuari usuari,
            @RequestParam(value = "fotoFile", required = false) MultipartFile fotoFile,
            @RequestParam(value = "confirmarContrasenya", required = false) String confirmarContrasenya,
            Model model) {
        try {
            log.info("Desant modificació de l'usuari: {}", usuari.getEmail());
            Usuari usuariAntic = usuariLogica.getUsuari(usuari.getEmail());
            if (usuariAntic == null) {
                throw new RuntimeException("L'usuari no existeix.");
            }

            if (fotoFile == null || fotoFile.isEmpty()) {
                usuari.setFoto(usuariAntic.getFoto());
            } else {
                usuari.setFoto(fotoFile.getBytes());
            }

            if (usuari.getContrasenya() == null || usuari.getContrasenya().isEmpty()) {
                usuari.setContrasenya(usuariAntic.getContrasenya());
            } else if (!usuari.getContrasenya().equals(confirmarContrasenya)) {
                throw new RuntimeException("Les contrasenyes no coincideixen.");
            }

            usuariLogica.modificarUsuari(usuari);
            return "redirect:/usuaris/consulta/" + usuari.getEmail();
        } catch (Exception e) {
            log.error("Error en desar la modificació de l'usuari", e);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("usuari", usuari);

            if (usuari.getFoto() != null) {
                String imatgeBase64 = Base64.getEncoder().encodeToString(usuari.getFoto());
                model.addAttribute("imatgeBase64", imatgeBase64);
            } else {
                model.addAttribute("imatgeBase64", null);
            }

            return "usuari-modificar";
        }
    }
}