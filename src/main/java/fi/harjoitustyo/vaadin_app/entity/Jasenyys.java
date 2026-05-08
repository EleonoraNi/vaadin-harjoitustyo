package fi.harjoitustyo.vaadin_app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Entity
@Table(name = "jasenyys")
public class Jasenyys {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @NotNull
    private LocalDate alkamisPaiva;

    @NotNull
    private LocalDate paattymisPaiva;

    @Min(1)
    @Max(3)
    private Integer taso;

    @NotBlank
    @Size(min = 2, max = 30)
    private String tyyppi;

    @NotBlank
    @Size(min = 2, max = 50)
    private String kaupunki;


    @NotNull
    private boolean voimassa;

    @OneToOne
    @JoinColumn(name = "liikkuja_id", nullable = false, unique = true)
    private Liikkuja liikkuja;

    public Jasenyys() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getAlkamisPaiva() {
        return alkamisPaiva;
    }

    public void setAlkamisPaiva(LocalDate alkamisPaiva) {
        this.alkamisPaiva = alkamisPaiva;
    }

    public LocalDate getPaattymisPaiva() {
        return paattymisPaiva;
    }

    public void setPaattymisPaiva(LocalDate paattymisPaiva) {
        this.paattymisPaiva = paattymisPaiva;
    }

    public int getTaso() {
        return taso;
    }

    public void setTaso(int taso) {
        this.taso = taso;
    }
        public String getTyyppi() {
        return tyyppi;
    }

    public void setTyyppi(String tyyppi) {
        this.tyyppi = tyyppi;
    }
    
    public String getKaupunki() {
        return kaupunki;
    }

    public void setKaupunki(String kaupunki) {
        this.kaupunki = kaupunki;
    }

    @Transient
    public boolean isVoimassa() {

        if (taso == null || alkamisPaiva == null || paattymisPaiva == null) {
            return false;
        }
        LocalDate today = LocalDate.now();
        return today.isEqual(alkamisPaiva) || (today.isAfter(alkamisPaiva) && today.isBefore(paattymisPaiva))
                || today.isEqual(paattymisPaiva);

    }

    public void setVoimassa(boolean voimassa) {
        this.voimassa = voimassa;
    }

    public Liikkuja getLiikkuja() {
        return liikkuja;
    }

    public void setLiikkuja(Liikkuja liikkuja) {
        this.liikkuja = liikkuja;
    }

    @AssertTrue
    public boolean isDatesValid() {
        return alkamisPaiva != null &&
                paattymisPaiva != null &&
                alkamisPaiva.isBefore(paattymisPaiva);
    }

}
