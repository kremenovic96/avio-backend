package com.letovi.cijeneletova.services;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOffer;
import com.google.gson.JsonElement;
import com.letovi.cijeneletova.models.SearchResult;
import com.letovi.cijeneletova.repositories.SearchResultRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component
public class SearchResultService {

    private static final String DEPARTURE = "DEPARTURE";
    private static final String ARRIVAL = "ARRIVAL";

    @Autowired
    SearchResultRepo searchResultRepo;

    @Autowired
    Amadeus amadeus;

    @Autowired
    AirPortAndCityService airPortAndCityService;

    /**
     * @param flightOffer
     * @param iataCode
     * @return full airport name based on its iataCode
     */
    private String getDetailedNameFromIataCode(FlightOffer flightOffer, String iataCode, String place) throws ResponseException {
        //user entered iataCode for example JFK
        if (flightOffer.getResponse().getResult().getAsJsonObject("dictionaries").get("locations").getAsJsonObject().get(iataCode.toUpperCase()) != null) {
            JsonElement airPortObj = flightOffer.getResponse().getResult().getAsJsonObject("dictionaries").get("locations").getAsJsonObject().get(iataCode).getAsJsonObject().get("detailedName");
            return airPortObj != null ? airPortObj.toString()
                    .replaceAll("\"", "") : "Nedostupno";
        }
        //user entered code. like  NYC
        else {
            String arrivalAirPortIataCode = getAirPortIataCode(flightOffer, place);
            return airPortAndCityService.getAirPortDetailedNameByKeyWord(arrivalAirPortIataCode);
        }
    }

    /***
     *
     * @param flightOffer
     * @param place with value of ARRIVAL or DEPARTURE
     * @return iata code of found airport
     */
    private String getAirPortIataCode(FlightOffer flightOffer, String place) {
        switch (place) {
            case "ARRIVAL":
                return getArrivalAirPortIataCode(flightOffer);
            case "DEPARTURE":
                return getDepartureAirPortIataCode(flightOffer);
            default:
                throw new IllegalArgumentException("Place " + place + " not supported. Should be ARRIVAL or DEPARTURE");
        }
    }

    private String getDepartureAirPortIataCode(FlightOffer flightOffer) {
        String departureIata = flightOffer.getOfferItems()[0].getServices()[0].getSegments()[0].getFlightSegment().getDeparture().getIataCode();
        return departureIata;
    }

    private String getArrivalAirPortIataCode(FlightOffer flightOffer) {
        int lastFlightSegmentIndex = flightOffer.getOfferItems()[0].getServices()[0].getSegments().length - 1;
        String arrivalIata = flightOffer.getOfferItems()[0].getServices()[0].getSegments()[lastFlightSegmentIndex].getFlightSegment().getArrival().getIataCode();
        return arrivalIata;
    }

    private Double getTotalPrice(FlightOffer flightOffer) {
        System.out.println(flightOffer.getOfferItems().length + " OfferItems len");
        return flightOffer.getOfferItems()[0].getPrice().getTotal();
    }

    private int getNumberOfStopsFromOrigin(FlightOffer flightOffer) {
        return flightOffer.getOfferItems()[0].getServices()[0].getSegments().length - 1;
    }

    private int getNumberOfStopsFromDestination(FlightOffer flightOffer) {
        return flightOffer.getOfferItems()[0].getServices()[1].getSegments().length - 1;
    }

    /**
     *
     * @param searchCriteria
     * @return existing SearchResult with same searchCriteria, or new if its not existent(saves it also)
     * @throws ResponseException
     */
    public List<SearchResult> getCachedOrNew(Map<String, String> searchCriteria) throws ResponseException {

        String origin = searchCriteria.get("origin");
        String destination = searchCriteria.get("destination");
        Integer numberOfAdults = Integer.valueOf(searchCriteria.get("adults"));
        LocalDate departureDate = LocalDate.parse(searchCriteria.get("departureDate"));
        LocalDate returnDate = LocalDate.parse(searchCriteria.get("returnDate"));
        //number of results to get
        String max = searchCriteria.getOrDefault("max", "10");
        List<SearchResult> cachedSearchResults = searchResultRepo.findBySameFields(origin.toUpperCase(), destination.toUpperCase(), numberOfAdults, departureDate, returnDate);
        if (!cachedSearchResults.isEmpty()) {
            return cachedSearchResults;
        }
        return createNewSearchResult(origin.toUpperCase(), destination.toUpperCase(), departureDate, returnDate, numberOfAdults, max);
    }


    private List<SearchResult> createNewSearchResult(String origin, String destination, LocalDate departureDate, LocalDate returnDate, Integer numberOfAdults,
                                  String max) throws ResponseException {

        FlightOffer[] flightOffers = amadeus.shopping.flightOffers.get(Params.with(
                "origin", origin)
                .and("destination", destination)
                .and("departureDate", departureDate)
                .and("returnDate", returnDate)
                .and("adults", numberOfAdults)
                .and("max", max));
        List<SearchResult> newlySavedSearchResults = new ArrayList<>();
        for(FlightOffer flightOffer : flightOffers) {
            //TODO: look into currency
            newlySavedSearchResults.add(saveSearchToDb(flightOffer, origin, destination, numberOfAdults, departureDate, returnDate, "EUR"));
        }
        return newlySavedSearchResults;
    }

    /**
     * @param flightOffer
     * @param originIataCode
     * @param departureIataCode
     * @param numberOfAdults
     * @param departureDate
     * @param returnDate
     * @param currency
     * @return SearchResult with its fields populated with api call results
     */
    private SearchResult saveSearchToDb(FlightOffer flightOffer, String originIataCode, String departureIataCode, Integer numberOfAdults, LocalDate departureDate, LocalDate returnDate,
                                       String currency) throws ResponseException {


        SearchResult searchResult = new SearchResult();
        searchResult.setBrojPutnika(numberOfAdults);
        searchResult.setDatumPolaska(departureDate);
        searchResult.setDatumPovratka(returnDate);
        searchResult.setValuta(currency);
        searchResult.setPolazniAerodromIataKod(originIataCode);
        searchResult.setOdredisniAerodromNazivIataKod(departureIataCode);
        searchResult.setPolazniAerodromNaziv(getDetailedNameFromIataCode(flightOffer, originIataCode, DEPARTURE));
        searchResult.setOdredisniAerodromNaziv(getDetailedNameFromIataCode(flightOffer, departureIataCode, ARRIVAL));
        searchResult.setUkupnaCijena(getTotalPrice(flightOffer));
        searchResult.setBrojPresjedanjaUOdlasku(getNumberOfStopsFromOrigin(flightOffer));
        searchResult.setBrojPresjedanjaUPovratku(getNumberOfStopsFromDestination(flightOffer));
        SearchResult savedSearch = searchResultRepo.save(searchResult);
        return savedSearch;

    }
}
