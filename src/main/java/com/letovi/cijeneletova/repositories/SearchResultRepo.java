package com.letovi.cijeneletova.repositories;

import com.letovi.cijeneletova.models.SearchResult;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SearchResultRepo extends CrudRepository<SearchResult, Integer> {

    @Query("select s from SearchResult s where s.polazniAerodromIataKod = :polazniAerodromIataKod and s.odredisniAerodromNazivIataKod" +
            " = :odredisniAerodromNazivIataKod and s.brojPutnika = :brojPutnika and s.datumPolaska = :datumPolaska and s.datumPovratka" +
            " = :datumPovratka")
    List<SearchResult> findBySameFields(@Param("polazniAerodromIataKod") String origin, @Param("odredisniAerodromNazivIataKod") String destination,
                                        @Param("brojPutnika") Integer adults, @Param("datumPolaska")LocalDate departureDate,
                                        @Param("datumPovratka") LocalDate returnDate);
}
