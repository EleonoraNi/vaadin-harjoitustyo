package fi.harjoitustyo.vaadin_app.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "liikuntatunti")
public class Liikuntatunti {
        
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nimi;

    @NotNull
    private LocalDateTime alkuaika;

    @NotNull
    private LocalDateTime loppuaika;

    @Min(1) @Max(30)
    private int kapasiteetti;

    @NotBlank
    private String tyyppi;

    // N:1 Ohjaaja
    @ManyToOne
    private Ohjaaja ohjaaja;

    // M:N Liikkujat
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "liikkuja_liikuntatunti",
        joinColumns = @JoinColumn(name = "liikuntatunti_id"),
        inverseJoinColumns = @JoinColumn(name = "liikkuja_id")
    )
    private Set<Liikkuja> liikkujat = new HashSet<>();

    public Liikuntatunti() {}

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

    public LocalDateTime getAlkuaika() {
        return alkuaika;
    }

    public void setAlkuaika(LocalDateTime alkuaika) {
        this.alkuaika = alkuaika;
    }

    public LocalDateTime getLoppuaika() {
        return loppuaika;
    }

    public void setLoppuaika(LocalDateTime loppuaika) {
        this.loppuaika = loppuaika;
    }

    public int getKapasiteetti() {
        return kapasiteetti;
    }

    public void setKapasiteetti(int kapasiteetti) {
        this.kapasiteetti = kapasiteetti;
    }

    public String getTyyppi() {
        return tyyppi;
    }

    public void setTyyppi(String tyyppi) {
        this.tyyppi = tyyppi;
    }

    public Ohjaaja getOhjaaja() {
        return ohjaaja;
    }

    public void setOhjaaja(Ohjaaja ohjaaja) {
        this.ohjaaja = ohjaaja;
    }

    public Set<Liikkuja> getLiikkujat() {
        return liikkujat;
    }

    public void setLiikkujat(Set<Liikkuja> liikkujat) {
        this.liikkujat = liikkujat;
    }

}
