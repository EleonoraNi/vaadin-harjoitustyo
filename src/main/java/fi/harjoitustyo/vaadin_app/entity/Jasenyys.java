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

    @Transient
    public boolean isVoimassa() {

        if (taso == null || alkamisPaiva == null || paattymisPaiva == null) {
            return false;
        }
        LocalDate today = LocalDate.now();
        return today.isEqual(alkamisPaiva) || (today.isAfter(alkamisPaiva) && today.isBefore(paattymisPaiva)) || today.isEqual(paattymisPaiva);

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
