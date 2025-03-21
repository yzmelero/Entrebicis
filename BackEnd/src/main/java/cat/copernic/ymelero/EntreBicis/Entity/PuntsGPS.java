package cat.copernic.ymelero.entrebicis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "puntsgps")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PuntsGPS {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ruta_id", nullable = false)
    private Ruta ruta;

    @Column(nullable = false)
    private Double latitud;

    @Column(nullable = false)
    private Double longitud;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "marca_temps", nullable = false)
    private LocalDate marcaTemps;

}
