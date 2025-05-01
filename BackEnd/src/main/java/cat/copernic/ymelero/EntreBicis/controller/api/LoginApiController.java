package cat.copernic.ymelero.entrebicis.controller.api;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.copernic.ymelero.entrebicis.entity.LoginRequest;
import cat.copernic.ymelero.entrebicis.entity.LoginResponse;
import cat.copernic.ymelero.entrebicis.entity.Usuari;
import cat.copernic.ymelero.entrebicis.repository.UsuariRepository;

@RestController
@RequestMapping("/api/login")
@CrossOrigin(origins = "*")
public class LoginApiController {

    @Autowired
    private UsuariRepository usuariRepository;

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody LoginRequest loginRequest) {

        if (loginRequest.getEmail() == null || loginRequest.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("El correu electrònic és obligatori.");
        }
        if (!loginRequest.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            return ResponseEntity.badRequest().body("El format del correu no és vàlid.");
        }
        if (loginRequest.getContrasenya() == null || loginRequest.getContrasenya().isEmpty()) {
            return ResponseEntity.badRequest().body("La contrasenya és obligatòria.");
        }
        Optional<Usuari> optionalUsuari = usuariRepository.findByEmail(loginRequest.getEmail());

        if (!optionalUsuari.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuari no trobat");
        }

        Usuari usuari = optionalUsuari.get();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!encoder.matches(loginRequest.getContrasenya(), usuari.getContrasenya())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contrasenya incorrecta");
        }

        LoginResponse response = new LoginResponse(
                usuari.getEmail(),
                usuari.getRol());

        return ResponseEntity.ok(response);
    }
}
