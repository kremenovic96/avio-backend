package com.letovi.cijeneletova.controllers;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.Response;
import com.amadeus.exceptions.ClientException;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.referenceData.Locations;
import com.amadeus.resources.*;
import com.amadeus.shopping.FlightOffers;
import com.amadeus.shopping.FlightOffersSearch;
import com.google.gson.JsonElement;
import com.letovi.cijeneletova.models.SearchResult;
import com.letovi.cijeneletova.repositories.SearchResultRepo;
import com.letovi.cijeneletova.services.SearchResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/home")
public class HomeController {

    @Autowired
    SearchResultRepo resultRepo;

    @Autowired
    SearchResultService searchResultService;

    @Autowired
    Amadeus amadeus;

    @PostMapping("/flightOffersSearches/")
    public ResponseEntity getFlightOffersSearches(@RequestBody Map<String, Object> searchCriteria) {
        try {
            FlightOfferSearch[] flightOffersSearches = amadeus.shopping.flightOffersSearch.get(
                    Params.with("originLocationCode", searchCriteria.get("originLocationCode"))
                            .and("destinationLocationCode", searchCriteria.get("destinationLocationCode"))
                            .and("departureDate", searchCriteria.get("departureDate"))
                            .and("returnDate", searchCriteria.get("returnDate"))
                            .and("adults", searchCriteria.get("adults"))
                            .and("max", searchCriteria.get("max")));

            //experimenting
            System.out.println(flightOffersSearches[0].getPrice());
            System.out.println(flightOffersSearches[0].getSource());

            System.out.println("ARRIVAL: " + flightOffersSearches[0].getItineraries()[0].getSegments()[0].getArrival());
            return ResponseEntity.ok(flightOffersSearches[0].getResponse().getBody());

        } catch (ResponseException e) {
            return ResponseEntity.badRequest().body(e.getDescription());

        }

    }

    @PostMapping("/getFlightOffers/")
    public ResponseEntity getFlightOffers(@RequestBody Map<String, Object> searchCriteria) {
        try {
            System.out.println(searchCriteria.get("departureDate"));
            FlightOffer[] flightOffer = amadeus.shopping.flightOffers.get(Params.with(
                    "origin", searchCriteria.get("origin"))
                    .and("destination", searchCriteria.get("destination"))
                    .and("departureDate", searchCriteria.get("departureDate"))
                    .and("returnDate", searchCriteria.get("returnDate"))
                    .and("adults", searchCriteria.get("adults"))
                    .and("max", searchCriteria.get("max")));
            System.out.println(flightOffer.length);
//                        return ResponseEntity.ok(flightOffer[0].getResponse().getBody());

            for (int i = 0; i < flightOffer.length; i++) {
                System.out.println(flightOffer[i].getResponse().getBody());
            }
            List<SearchResult> flights = new ArrayList<>();
            for (int i = 0; i < flightOffer.length; i++) {
                SearchResult searchResult = searchResultService.pushResultToDB(flightOffer[i], (String) searchCriteria.get("origin"),
                        (String) searchCriteria.get("destination"), Integer.valueOf((String) searchCriteria.get("adults")), (String) searchCriteria.get("departureDate"),
                        (String) searchCriteria.get("returnDate"), "EUR");
                flights.add(searchResult);
            }

            return ResponseEntity.ok(flights);
//            return ResponseEntity.ok(flightOffer[0].getResponse().getBody());
        } catch (ResponseException e) {
            return ResponseEntity.badRequest().body(e.getDescription());
//            return ResponseEntity.badRequest(e.getDescription()).build();
        }
    }

    @PostMapping("/locations/")
    public ResponseEntity location(@RequestBody Map<String, Object> searchCriteria) {

        Location[] locations = new Location[0];
        try {
            locations = amadeus.referenceData.locations.get(Params
                    .with((String) searchCriteria.get("key"), searchCriteria.get("keyword"))
//                    .and("keyword", "LHR")
                    .and("subType", Locations.AIRPORT));


        } catch (ResponseException e) {
            return ResponseEntity.badRequest().body(e.getDescription());
        }
        System.out.println(locations.length);
        for (int i = 0; i < locations.length; i++) {
            System.out.println(locations[i].getIataCode());
            System.out.println(locations[i]);
        }
        return ResponseEntity.ok(locations[0].getResponse().getBody());

    }
}
