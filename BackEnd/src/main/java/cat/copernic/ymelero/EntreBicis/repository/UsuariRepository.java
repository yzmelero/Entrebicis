package cat.copernic.ymelero.entrebicis.repository;

import cat.copernic.ymelero.entrebicis.entity.Usuari;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuariRepository extends JpaRepository<Usuari, String> {
    Optional<Usuari> findByEmail(String email);
}