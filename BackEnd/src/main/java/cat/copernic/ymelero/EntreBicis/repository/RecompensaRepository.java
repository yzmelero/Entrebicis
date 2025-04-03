package cat.copernic.ymelero.entrebicis.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cat.copernic.ymelero.entrebicis.entity.Recompensa;

public interface RecompensaRepository extends JpaRepository<Recompensa, Long> {

    Optional<Recompensa> findById(Long id);
}
