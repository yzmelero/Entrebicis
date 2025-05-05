package cat.copernic.ymelero.entrebicis.entity;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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

    private String nom;

    private String cognoms;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_naixement")
    private LocalDate dataNaixement;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] foto;

    private String poblacio;

    @Column(unique = true)
    private String telefon;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    private Integer saldo;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_alta")
    private LocalDate dataAlta;

    private String contrasenya;

    private String observacions;

}
