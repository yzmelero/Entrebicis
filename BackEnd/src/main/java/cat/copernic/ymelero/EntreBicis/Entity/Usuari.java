package cat.copernic.ymelero.entrebicis.entity;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuari")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuari {

    @Id
    private String email;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String cognoms;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_naixement", nullable = false)
    private LocalDate dataNaixement;

    @Column(nullable = false)
    private byte[] foto;

    @Column(nullable = false)
    private String poblacio;

    @Column(unique = true)
    private String telefon;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    @Column(nullable = false)
    private Double saldo;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_alta", nullable = false)
    private LocalDate dataAlta;

    @Column(nullable = false)
    private String contrasenya;

    @Column(nullable = false)
    private String observacions;

}
