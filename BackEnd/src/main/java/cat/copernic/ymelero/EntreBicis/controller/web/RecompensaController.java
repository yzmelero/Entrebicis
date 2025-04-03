package cat.copernic.ymelero.entrebicis.controller.web;

import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cat.copernic.ymelero.entrebicis.entity.Recompensa;
import cat.copernic.ymelero.entrebicis.logic.web.RecompensaLogica;

@Controller
@RequestMapping("/recompenses")
public class RecompensaController {

    @Autowired
    private RecompensaLogica recompensaLogica;

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

    @GetMapping("/crear")
    public String crearRecompensa(Model model) {
        model.addAttribute("recompensa", new Recompensa());
        return "recompensa-alta";
    }

    @PostMapping("/crear")

    public String guardarNovaRecompensa(@ModelAttribute Recompensa recompensa,
            @RequestParam(value = "fotoFile", required = false) MultipartFile fotoFile,
            Model model) {
        try {
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
            model.addAttribute("error", e.getMessage());
            model.addAttribute("recompensa", recompensa);
            return "recompensa-alta";
        }
    }

}
