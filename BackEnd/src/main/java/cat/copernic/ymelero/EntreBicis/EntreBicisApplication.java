package cat.copernic.ymelero.entrebicis;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import cat.copernic.ymelero.entrebicis.entity.Rol;
import cat.copernic.ymelero.entrebicis.entity.Usuari;
import cat.copernic.ymelero.entrebicis.repository.UsuariRepository;

@SpringBootApplication
public class EntreBicisApplication {

	public static void main(String[] args) {
		SpringApplication.run(EntreBicisApplication.class, args);
	}

	@Bean
	CommandLineRunner initAdmin(UsuariRepository usuariRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (usuariRepository.findByEmail("admin@admin.com").isEmpty()) {
				Usuari admin = new Usuari();
				admin.setEmail("admin@admin.com");
				admin.setNom("Admin");
				admin.setCognoms("Administrador");
				admin.setDataNaixement(LocalDate.of(2000, 1, 1));
				admin.setFoto(null);
				admin.setPoblacio("Ciutat");
				admin.setTelefon("123456789");
				admin.setRol(Rol.ADMIN);
				admin.setSaldo(0.0);
				admin.setDataAlta(LocalDate.now());
				admin.setContrasenya(passwordEncoder.encode("admin123"));
				admin.setObservacions("Administrador del sistema");

				usuariRepository.save(admin);
				System.out.println("Administrador creat correctament.");
			} else {
				System.out.println("L'admin ja existeix.");
			}
		};
	}
}
