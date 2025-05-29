package cat.copernic.ymelero.entrebicis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cat.copernic.ymelero.entrebicis.entity.PuntGPS;

public interface PuntsRepository extends JpaRepository<PuntGPS, Long> {

    List<PuntGPS> findByRutaIdOrderByMarcaTempsAsc(Long idRuta);
}
