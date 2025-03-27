package cat.copernic.ymelero.entrebicis.entity;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recompensa")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recompensa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String descripcio;

    @Column(nullable = false)
    private String observacions;

    @Column(nullable = false)
    private Double punts;

    @Column(nullable = false)
    private EstatRecompensa estat;

    @Column(name = "nom_comerç", nullable = false)
    private String nomComerç;

    @Column(name = "adreça_comerç", nullable = false)
    private String adreçaComerç;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Usuari usuari;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_reserva", nullable = false)
    private LocalDate dataReserva;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_recollida", nullable = false)
    private LocalDate dataRecollida;
}
