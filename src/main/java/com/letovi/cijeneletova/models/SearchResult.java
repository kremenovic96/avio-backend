package com.letovi.cijeneletova.models;

import javax.persistence.*;
import java.time.LocalDate;

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
    private LocalDate datumPolaska;
    @Column
    private LocalDate datumPovratka;
//    private Date datumPovratka;
    @Column
    private Integer brojPutnika;
    @Column
    private String valuta;
    @Column
    private Double ukupnaCijena;

    private Integer brojPresjedanjaUOdlasku;
    private Integer brojPresjedanjaUPovratku;

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

    public LocalDate getDatumPolaska() {
        return datumPolaska;
    }

    public void setDatumPolaska(LocalDate datumPolaska) {
        this.datumPolaska = datumPolaska;
    }

    public LocalDate getDatumPovratka() {
        return datumPovratka;
    }

    public void setDatumPovratka(LocalDate datumPovratka) {
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

    public Integer getBrojPresjedanjaUOdlasku() {
        return brojPresjedanjaUOdlasku;
    }

    public void setBrojPresjedanjaUOdlasku(Integer brojPresjedanjaUOdlasku) {
        this.brojPresjedanjaUOdlasku = brojPresjedanjaUOdlasku;
    }

    public Integer getBrojPresjedanjaUPovratku() {
        return brojPresjedanjaUPovratku;
    }

    public void setBrojPresjedanjaUPovratku(Integer brojPresjedanjaUPovratku) {
        this.brojPresjedanjaUPovratku = brojPresjedanjaUPovratku;
    }

    public Integer getResultId() {
        return resultId;
    }
}
