package com.letovi.cijeneletova.services;

import com.amadeus.resources.FlightDate;
import com.amadeus.resources.FlightOffer;
import com.amadeus.resources.Resource;
import com.letovi.cijeneletova.models.SearchResult;
import com.letovi.cijeneletova.repositories.SearchResultRepo;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class SearchResultService {

    @Autowired
    SearchResultRepo searchResultRepo;

    /**
     * @param flightOffer
     * @param iataCode
     * @return full airport name based on its iataCode
     */
    public String getDetailedNameFromIataCode(FlightOffer flightOffer, String iataCode) {
        String detailedName = flightOffer.getResponse().getResult().getAsJsonObject("dictionaries").get("locations").getAsJsonObject().get(iataCode).getAsJsonObject().get("detailedName").toString()
                .replaceAll("\"", "");
        return detailedName;
    }


    public Double getTotalPrice(FlightOffer flightOffer) {
        System.out.println(flightOffer.getOfferItems().length + " OfferItems len");
        return flightOffer.getOfferItems()[0].getPrice().getTotal();
    }

    public int getNumberOfStopsFromOrigin(FlightOffer flightOffer) {
        return flightOffer.getOfferItems()[0].getServices()[0].getSegments().length - 1;
    }

    public int getNumberOfStopsFromDestination(FlightOffer flightOffer) {
        return flightOffer.getOfferItems()[0].getServices()[1].getSegments().length - 1;
    }

    /**
     * @param flightOffer
     * @param polazniAerodromIataCode
     * @param odredisniAerodromIataCode
     * @param brojPutnika
     * @param datumPolaska
     * @param datumPovratka
     * @param valuta
     * @return SearchResult with its fields populated with api call result
     */
    public SearchResult pushResultToDB(FlightOffer flightOffer, String polazniAerodromIataCode, String odredisniAerodromIataCode, Integer brojPutnika, String datumPolaska, String datumPovratka,
                                       String valuta) {

        //parse string to localdate
        LocalDate datumPovratkaDate = LocalDate.parse(datumPovratka);
        LocalDate datumPolaskaDate = LocalDate.parse(datumPolaska);

        SearchResult searchResult = new SearchResult();
        searchResult.setBrojPutnika(brojPutnika);
        searchResult.setDatumPolaska(datumPolaskaDate);
        searchResult.setDatumPovratka(datumPovratkaDate);
        searchResult.setValuta(valuta);
        searchResult.setPolazniAerodromIataKod(polazniAerodromIataCode);
        searchResult.setOdredisniAerodromNazivIataKod(odredisniAerodromIataCode);
        searchResult.setPolazniAerodromNaziv(getDetailedNameFromIataCode(flightOffer, polazniAerodromIataCode));
        searchResult.setOdredisniAerodromNaziv(getDetailedNameFromIataCode(flightOffer, odredisniAerodromIataCode));
        searchResult.setUkupnaCijena(getTotalPrice(flightOffer));
        searchResult.setBrojPresjedanjaUOdlasku(getNumberOfStopsFromOrigin(flightOffer));
        searchResult.setBrojPresjedanjaUPovratku(getNumberOfStopsFromDestination(flightOffer));
        SearchResult savedSearch = searchResultRepo.save(searchResult);
        return savedSearch;
    }
}
