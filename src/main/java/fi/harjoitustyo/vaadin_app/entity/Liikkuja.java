package fi.harjoitustyo.vaadin_app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "liikkuja")

public class Liikkuja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Version
    private Long version;

    @NotBlank
    @Size(min = 2, max = 50)
    private String etunimi;

    @NotBlank
    @Size(min = 2, max = 50)
    private String sukunimi;

    @Email
    @Column(unique = true)
    private String email;

    @Past
    @NotNull
    private LocalDate syntymaAika;

    @Size(max = 20)
    private String puhelin;

    // 1:1 Jäsenyys
    @OneToOne(mappedBy = "liikkuja", cascade = CascadeType.ALL)
    private Jasenyys jasenyys;

    // M:N Liikuntatunnit
    @ManyToMany(mappedBy = "liikkujat")
    private Set<Liikuntatunti> liikuntatunnit = new HashSet<>();

    public Liikkuja() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEtunimi() {
        return etunimi;
    }

    public void setEtunimi(String etunimi) {
        this.etunimi = etunimi;
    }

    public String getSukunimi() {
        return sukunimi;
    }

    public void setSukunimi(String sukunimi) {
        this.sukunimi = sukunimi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getSyntymaAika() {
        return syntymaAika;
    }

    public void setSyntymaAika(LocalDate syntymaAika) {
        this.syntymaAika = syntymaAika;
    }

    public String getPuhelin() {
        return puhelin;
    }

    public void setPuhelin(String puhelin) {
        this.puhelin = puhelin;
    }

    public Jasenyys getJasenyys() {
        return jasenyys;
    }

    public void setJasenyys(Jasenyys jasenyys) {
        this.jasenyys = jasenyys;
    }

    public Set<Liikuntatunti> getLiikuntatunnit() {
        return liikuntatunnit;
    }

    public void setLiikuntatunnit(Set<Liikuntatunti> liikuntatunnit) {
        this.liikuntatunnit = liikuntatunnit;
    }
}
