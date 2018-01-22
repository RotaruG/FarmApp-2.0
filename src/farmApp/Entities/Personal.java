package farmApp.Entities;

import java.sql.Timestamp;

/**
 *
 * @author Ghetto
 */
public class Personal {


private int id;
private String nume;
private String prenume;
private String functia;
private Boolean statut;
private Timestamp dataAngajare;
private Timestamp dataConcediere;

    public Personal() {
    }

    public Personal(String nume, String prenume, String functia, Boolean statut, Timestamp dataAngajare, Timestamp dataConcediere) {
        this.nume = nume;
        this.prenume = prenume;
        this.functia = functia;
        this.statut = statut;
        this.dataAngajare = dataAngajare;
        this.dataConcediere = dataConcediere;
    }

    public Personal(int id, String nume, String prenume, String functia, Boolean statut, Timestamp dataAngajare, Timestamp dataConcediere) {
        this.id = id;
        this.nume = nume;
        this.prenume = prenume;
        this.functia = functia;
        this.statut = statut;
        this.dataAngajare = dataAngajare;
        this.dataConcediere = dataConcediere;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getFunctia() {
        return functia;
    }

    public void setFunctia(String functia) {
        this.functia = functia;
    }

    public Boolean getStatut() {
        return statut;
    }

    public void setStatut(Boolean statut) {
        this.statut = statut;
    }

    public Timestamp getDataAngajare() {
        return dataAngajare;
    }

    public void setDataAngajare(Timestamp dataAngajare) {
        this.dataAngajare = dataAngajare;
    }

    public Timestamp getDataConcediere() {
        return dataConcediere;
    }

    public void setDataConcediere(Timestamp dataConcediere) {
        this.dataConcediere = dataConcediere;
    }

    @Override
    public String toString() {
        return "Personal{" + "id=" + id + ", nume=" + nume + ", prenume=" + prenume + ", functia=" + functia + ", statut=" + statut + ", dataAngajare=" + dataAngajare + ", dataConcediere=" + dataConcediere + '}';
    }

    

    
}
