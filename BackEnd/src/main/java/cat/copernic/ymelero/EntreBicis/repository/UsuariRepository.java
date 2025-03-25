package cat.copernic.ymelero.entrebicis.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cat.copernic.ymelero.entrebicis.entity.Usuari;

public interface UsuariRepository extends JpaRepository<Usuari, String> {
    Optional<Usuari> findByEmail(String email);
}