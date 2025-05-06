package cat.copernic.ymelero.entrebicis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cat.copernic.ymelero.entrebicis.entity.PuntGPS;

public interface PuntsRepository extends JpaRepository<PuntGPS, Long> {
}
