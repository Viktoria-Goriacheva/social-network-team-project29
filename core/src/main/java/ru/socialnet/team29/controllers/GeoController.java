package ru.socialnet.team29.controllers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.socialnet.team29.responses.GeoCitiesResponse;
import ru.socialnet.team29.responses.GeoCountriesResponse;
import ru.socialnet.team29.service.GeoService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/geo")
public class GeoController {

  private final GeoService geoService;

  @GetMapping("/countries")
  public ResponseEntity<List<GeoCountriesResponse>> getCountry() {
    return new ResponseEntity<>(geoService.getCountries(), HttpStatus.OK);
  }

  @GetMapping("/cities/{countryId}")
  public ResponseEntity<List<GeoCitiesResponse>> getCountryById(@PathVariable Integer countryId) {
    return new ResponseEntity<>(geoService.getAddresses(countryId), HttpStatus.OK);
  }
}
