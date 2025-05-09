package cat.copernic.ymelero.entrebicis.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ruta")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ruta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Usuari usuari;

    private EstatRuta estat;

    private Double distancia;

    @Column(name = "temps_total", nullable = false)
    private Double tempsTotal;

    @Column(name = "velocitat_maxima", nullable = false)
    private Double velocitatMaxima;

    @Column(name = "velocitat_mitjana", nullable = false)
    private Double velocitatMitjana;

    @OneToMany(mappedBy = "ruta", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<PuntGPS> puntGPS = new ArrayList<>();

    private LocalDate dataCreacio;

    private boolean validada;
}
