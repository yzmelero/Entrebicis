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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cat.copernic.ymelero.entrebicis.entity.Recompensa;
import cat.copernic.ymelero.entrebicis.entity.Usuari;
import cat.copernic.ymelero.entrebicis.logic.web.RecompensaLogica;

@Controller
@RequestMapping("/recompenses")
public class RecompensaController {

    private static final Logger log = LoggerFactory.getLogger(RecompensaController.class);

    @Autowired
    private RecompensaLogica recompensaLogica;

    /**
     * Mètode per mostrar el llistat de recompenses disponibles.
     *
     * @param model Model per passar dades a la vista.
     * @return Nom de la vista a mostrar.
     */
    @GetMapping
    public String llistarRecompenses(Model model) {
        List<Recompensa> recompenses = recompensaLogica.getAllRecompenses();
        Map<Long, String> imatgesBase64 = new HashMap<>();

        for (Recompensa recompensa : recompenses) {
            if (recompensa.getFoto() != null) {
                String imatgeBase64 = Base64.getEncoder().encodeToString(recompensa.getFoto());
                imatgesBase64.put(recompensa.getId(), imatgeBase64);
            }
        }
        model.addAttribute("recompenses", recompenses);
        model.addAttribute("imatgesBase64", imatgesBase64);
        return "recompensa-llistar";
    }

    /**
     * Mètode per mostrar el formulari de creació d'una nova recompensa.
     *
     * @param model Model per passar dades a la vista.
     * @return Nom de la vista a mostrar.
     */
    @GetMapping("/crear")
    public String crearRecompensa(Model model) {
        log.info("Creant nova recompensa");
        model.addAttribute("recompensa", new Recompensa());
        return "recompensa-alta";
    }

    /**
     * Mètode per guardar una nova recompensa.
     *
     * @param recompensa L'objecte recompensa a guardar.
     * @param fotoFile   Fitxer d'imatge de la recompensa.
     * @param model      Model per passar dades a la vista.
     * @return Redirecció a la vista de llistat de recompenses.
     */
    @PostMapping("/crear")
    public String guardarNovaRecompensa(@ModelAttribute Recompensa recompensa,
            @RequestParam(value = "fotoFile", required = false) MultipartFile fotoFile,
            Model model) {
        try {
            log.info("Desant nova recompensa: {}", recompensa);
            if (fotoFile != null && !fotoFile.isEmpty()) {
                recompensa.setFoto(fotoFile.getBytes());
            } else {
                ClassPathResource iconReward = new ClassPathResource("static/img/iconReward.png");
                byte[] defaultImage = Files.readAllBytes(iconReward.getFile().toPath());
                recompensa.setFoto(defaultImage);
            }
            recompensaLogica.crearRecompensa(recompensa);
            return "redirect:/recompenses";
        } catch (Exception e) {
            log.error("Error en desar la recompensa", e);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("recompensa", recompensa);
            return "recompensa-alta";
        }
    }

    /**
     * Mètode per mostrar una recompensa existent.
     *
     * @param id    ID de la recompensa.
     * @param model Model per passar dades a la vista.
     * @return Nom de la vista a mostrar.
     */
    @GetMapping("/consultar/{id}")
    public String mostrarRecompensa(@PathVariable Long id, Model model) {
        Recompensa recompensa = recompensaLogica.getRecompensa(id);
        log.info("Consultant recompensa amb ID: {}", id);
        try {

            if (recompensa == null) {
                model.addAttribute("error", "No s'ha trobat la recompensa amb ID: " + id);
                return "redirect:/recompenses";
            }

            if (recompensa.getFoto() != null) {
                String imatgeBase64 = Base64.getEncoder().encodeToString(recompensa.getFoto());
                model.addAttribute("imatgeBase64", imatgeBase64);
            } else {
                model.addAttribute("imatgeBase64", null);
            }
            model.addAttribute("recompensa", recompensa);

            return "recompensa-consultar";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/recompenses";
        }
    }

    /**
     * Mètode per esborrar una recompensa existent.
     * 
     * @param id    ID de la recompensa a esborrar.
     * @param model Model per passar dades a la vista.
     * @return Redirecció a la vista de llistat de recompenses.
     */
    @GetMapping("/esborrar/{id}")
    public String esborrarRecompensa(@PathVariable Long id, Model model) {
        try {
            log.info("Esborrant recompensa amb ID: {}", id);
            recompensaLogica.eliminarRecompensa(id);
            return "redirect:/recompenses";
        } catch (Exception e) {
            log.error("Error en esborrar recompensa ID {}: {}", id, e.getMessage(), e);
            model.addAttribute("error", e.getMessage());
            return "redirect:/recompenses";
        }
    }

    /**
     * Mètode per assignar una recompensa a un usuari.
     * 
     * @param id    ID de la recompensa a assignar.
     * @param model Model per passar dades a la vista.
     * @return Redirecció a la vista de consulta de la recompensa.
     */
    @PostMapping("/assignar/{id}")
    public String assignarRecompensa(@PathVariable Long id, Model model) {
        try {
            log.info("Assignant recompensa amb ID: {}", id);
            recompensaLogica.assignarRecompensa(id);
            return "redirect:/recompenses/consultar/" + id;
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/recompenses/consultar/" + id;
        }
    }

    /**
     * Mètode per mostrar l'historial de recompenses d'un usuari.
     * 
     * @param email L'email de l'usuari.
     * @param model Model per passar dades a la vista.
     * @return Nom de la vista a mostrar.
     */
    @GetMapping("/historial/{email}")
    public String veureHistorialRecompenses(@PathVariable String email, Model model) {
        List<Recompensa> recompenses = recompensaLogica.getRecompensesPropies(email);
        Map<Long, String> imatgesBase64 = new HashMap<>();
        log.info("Consultant historial de recompenses per a: {}", email);

        for (Recompensa recompensa : recompenses) {
            if (recompensa.getFoto() != null) {
                String imatgeBase64 = Base64.getEncoder().encodeToString(recompensa.getFoto());
                imatgesBase64.put(recompensa.getId(), imatgeBase64);
            }
        }

        model.addAttribute("recompenses", recompenses);
        model.addAttribute("imatgesBase64", imatgesBase64);

        Usuari usuari = recompensaLogica.getUsuariPerEmail(email);
        model.addAttribute("usuariRecompenses", usuari);

        return "recompensa-llistar";
    }

}
