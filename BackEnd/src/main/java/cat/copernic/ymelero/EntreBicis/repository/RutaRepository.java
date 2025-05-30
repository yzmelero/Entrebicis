package cat.copernic.ymelero.entrebicis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cat.copernic.ymelero.entrebicis.entity.Ruta;

public interface RutaRepository extends JpaRepository<Ruta, Long> {
    List<Ruta> findByUsuariEmailOrderByDataCreacioDesc(String email);

    List<Ruta> findAllByOrderByDataCreacioDesc();

    List<Ruta> findByUsuari_Email(String email);

}