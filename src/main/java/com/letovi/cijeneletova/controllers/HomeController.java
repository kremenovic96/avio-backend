package com.letovi.cijeneletova.controllers;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.referenceData.Locations;
import com.amadeus.resources.*;
import com.letovi.cijeneletova.models.SearchResult;
import com.letovi.cijeneletova.repositories.SearchResultRepo;
import com.letovi.cijeneletova.services.SearchResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    /**
     * For future use
     * @param searchCriteria
     * @return
     */
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

            System.out.println("ARRIVAL: " + flightOffersSearches[0].getItineraries()[0].getSegments()[0].getArrival());
            return ResponseEntity.ok(flightOffersSearches[0].getResponse().getBody());

        } catch (ResponseException e) {
            return ResponseEntity.badRequest().body(e.getDescription());

        }

    }

    @PostMapping("/getFlightOffers/")
    public ResponseEntity getFlightOffers(@RequestBody Map<String, String> searchCriteria) {

        try {
            List<SearchResult> searchResults = searchResultService.getCachedOrNew(searchCriteria);
            return ResponseEntity.ok(searchResults);

        } catch (ResponseException e) {
            return ResponseEntity.badRequest().body(e.getDescription());
        }
    }

    /**
     * For future use
     * @param searchCriteria
     * @return
     */
    @PostMapping("/locations/")
    public ResponseEntity airPortSearch(@RequestBody Map<String, Object> searchCriteria) {

        Location[] locations = new Location[0];
        try {
            locations = amadeus.referenceData.locations.get(Params
                    .with((String) searchCriteria.get("key"), searchCriteria.get("keyword"))
//                    .and("keyword", "LHR")
                    .and("subType", Locations.AIRPORT));


        } catch (ResponseException e) {
            return ResponseEntity.badRequest().body(e.getDescription());
        }
        return ResponseEntity.ok(locations[0].getResponse().getBody());

    }

}
