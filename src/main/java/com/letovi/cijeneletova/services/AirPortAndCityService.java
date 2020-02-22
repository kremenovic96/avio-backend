package com.letovi.cijeneletova.services;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.referenceData.Locations;
import com.amadeus.resources.Location;
import com.letovi.cijeneletova.models.AirPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AirPortAndCityService {

    @Autowired
    Amadeus amadeus;


    public String getAirPortDetailedNameByKeyWord(String keyword) throws ResponseException {

        Location[] locations = amadeus.referenceData.locations.get(Params
                .with("keyword", keyword)
                .and("subType", Locations.AIRPORT));
        return locations[0].getDetailedName().split(":")[1];
    }
}
