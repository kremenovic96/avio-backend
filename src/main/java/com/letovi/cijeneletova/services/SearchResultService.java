package com.letovi.cijeneletova.services;

import com.amadeus.resources.FlightDate;
import com.amadeus.resources.FlightOffer;
import com.amadeus.resources.Resource;
import com.letovi.cijeneletova.models.SearchResult;
import com.letovi.cijeneletova.repositories.SearchResultRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SearchResultService {

    @Autowired
    SearchResultRepo searchResultRepo;

    /**
     *
     * @param flightOffer
     * @param iataCode
     * @return full airport name based on its iataCode
     */
    public String getDetailedNameFromIataCode(FlightOffer flightOffer, String iataCode) {
        String detailedName = flightOffer.getResponse().getResult().getAsJsonObject("dictionaries").get("locations").getAsJsonObject().get(iataCode).getAsJsonObject().get("detailedName").toString();
        return detailedName;
    }


    public Double getTotalPrice(FlightOffer flightOffer) {
        return flightOffer.getOfferItems()[0].getPrice().getTotal();
    }

    /**
     *
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

        SearchResult searchResult = new SearchResult();
        searchResult.setBrojPutnika(brojPutnika);
        searchResult.setDatumPolaska(datumPolaska);
        searchResult.setDatumPovratka(datumPovratka);
        searchResult.setValuta(valuta);
        searchResult.setPolazniAerodromIataKod(polazniAerodromIataCode);
        searchResult.setOdredisniAerodromNazivIataKod(odredisniAerodromIataCode);
        searchResult.setPolazniAerodromNaziv(getDetailedNameFromIataCode(flightOffer, polazniAerodromIataCode));
        searchResult.setOdredisniAerodromNaziv(getDetailedNameFromIataCode(flightOffer, odredisniAerodromIataCode));
        searchResult.setUkupnaCijena(getTotalPrice(flightOffer));
        SearchResult savedSearch = searchResultRepo.save(searchResult);
        return savedSearch;
    }
}
