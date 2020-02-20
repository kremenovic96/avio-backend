package com.letovi.cijeneletova.models;

import javax.persistence.*;
import java.sql.Date;
@Entity
public class SearchResult {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer resultId;

    @Column
    private String polazniAerodromNaziv;
    @Column
    private String OdredisniAerodromNaziv;
    @Column
    private String polazniAerodromIataKod;
    @Column
    private String odredisniAerodromNazivIataKod;
    @Column
    private String datumPolaska;
    @Column
    private String datumPovratka;
//    private Date datumPovratka;
    @Column
    private Integer brojPutnika;
    @Column
    private String valuta;
    @Column
    private Double ukupnaCijena;

    public SearchResult() {
    }

    public String getPolazniAerodromNaziv() {
        return polazniAerodromNaziv;
    }

    public void setPolazniAerodromNaziv(String polazniAerodromNaziv) {
        this.polazniAerodromNaziv = polazniAerodromNaziv;
    }

    public String getOdredisniAerodromNaziv() {
        return OdredisniAerodromNaziv;
    }

    public void setOdredisniAerodromNaziv(String odredisniAerodromNaziv) {
        OdredisniAerodromNaziv = odredisniAerodromNaziv;
    }

    public String getPolazniAerodromIataKod() {
        return polazniAerodromIataKod;
    }

    public void setPolazniAerodromIataKod(String polazniAerodromIataKod) {
        this.polazniAerodromIataKod = polazniAerodromIataKod;
    }

    public String getOdredisniAerodromNazivIataKod() {
        return odredisniAerodromNazivIataKod;
    }

    public void setOdredisniAerodromNazivIataKod(String odredisniAerodromNazivIataKod) {
        this.odredisniAerodromNazivIataKod = odredisniAerodromNazivIataKod;
    }

    public String getDatumPolaska() {
        return datumPolaska;
    }

    public void setDatumPolaska(String datumPolaska) {
        this.datumPolaska = datumPolaska;
    }

    public String getDatumPovratka() {
        return datumPovratka;
    }

    public void setDatumPovratka(String datumPovratka) {
        this.datumPovratka = datumPovratka;
    }

    public Integer getBrojPutnika() {
        return brojPutnika;
    }

    public void setBrojPutnika(Integer brojPutnika) {
        this.brojPutnika = brojPutnika;
    }

    public String getValuta() {
        return valuta;
    }

    public void setValuta(String valuta) {
        this.valuta = valuta;
    }

    public Double getUkupnaCijena() {
        return ukupnaCijena;
    }

    public void setUkupnaCijena(Double ukupnaCijena) {
        this.ukupnaCijena = ukupnaCijena;
    }
}
