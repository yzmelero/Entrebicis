package cat.copernic.ymelero.entrebicis.entity;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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

    private String descripcio;

    private String observacions;

    private Double punts;

    @Enumerated(EnumType.STRING)
    private EstatRecompensa estat;

    @Column(name = "nom_comerç")
    private String nomComerç;

    @Column(name = "adreça_comerç")
    private String adreçaComerç;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] foto;

    @ManyToOne
    private Usuari usuari;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_reserva")
    private LocalDate dataReserva;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_recollida")
    private LocalDate dataRecollida;
}
