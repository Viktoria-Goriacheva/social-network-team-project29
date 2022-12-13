package ru.socialnet.team29.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.socialnet.team29.responses.GeoCitiesResponse;
import ru.socialnet.team29.responses.GeoCountriesResponse;
import ru.socialnet.team29.service.GeoService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/geo")
@Slf4j
public class GeoController {
    private final GeoService geoService;

    @GetMapping("/countries")
    public ResponseEntity<List<GeoCountriesResponse>> getCountry() {
        return new ResponseEntity<>(geoService.getCountries(), HttpStatus.OK);
    }

    @GetMapping("/cities/{countryId}")
    public ResponseEntity<List<GeoCitiesResponse>> getCountryById(@PathVariable Integer countryId) {
        log.info(countryId + " countryId");
        return new ResponseEntity<>(geoService.getAddresses().get(countryId), HttpStatus.OK);
    }
}
