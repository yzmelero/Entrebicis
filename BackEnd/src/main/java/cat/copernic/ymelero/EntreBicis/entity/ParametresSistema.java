package cat.copernic.ymelero.entrebicis.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parametres_sistema")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParametresSistema {

    @Id
    private Long id = 1L;

    private Double velocitatMaxima = 60.0;

    private Integer tempsMaximAturada = 5;

    private Double conversioQuilometreSaldo = 1.0;

    private Integer tempsMaximRecollidaRecompensa = 72;
}
