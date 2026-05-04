package fi.harjoitustyo.vaadin_app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;

@Entity
@Table(name = "ohjaaja")

public class Ohjaaja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 80)
    private String nimi;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String erikoistuminen;

    @Size(max = 20)
    private String puhelin;

    @OneToMany(mappedBy = "ohjaaja")
    private List<Liikuntatunti> liikuntatunnit;

    private String tiedostoPolku;

    public Ohjaaja() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getErikoistuminen() {
        return erikoistuminen;
    }

    public void setErikoistuminen(String erikoistuminen) {
        this.erikoistuminen = erikoistuminen;
    }

    public String getPuhelin() {
        return puhelin;
    }

    public void setPuhelin(String puhelin) {
        this.puhelin = puhelin;
    }

    public String getTiedostoPolku() {
        return tiedostoPolku;
    }

    public void setTiedostoPolku(String tiedostoPolku) {
        this.tiedostoPolku = tiedostoPolku;
    }

    public List<Liikuntatunti> getLiikuntatunnit() {
        return liikuntatunnit;
    }

    public void setLiikuntatunnit(List<Liikuntatunti> liikuntatunnit) {
        this.liikuntatunnit = liikuntatunnit;
    }

}
