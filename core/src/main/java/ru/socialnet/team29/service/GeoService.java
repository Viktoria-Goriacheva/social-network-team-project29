package ru.socialnet.team29.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.GeoLauncher;
import ru.socialnet.team29.responses.GeoCitiesResponse;
import ru.socialnet.team29.responses.GeoCountriesResponse;

@Service
@RequiredArgsConstructor
@Data
@Slf4j
public class GeoService {

  private final GeoLauncher geoLauncher;
  private List<GeoCountriesResponse> countries = new ArrayList<>();

  @Cacheable("countries")
  public List<GeoCountriesResponse> getCountries() {
    log.info("Выбраны страны");
    return geoLauncher.getCountries();
  }

  @Cacheable(value = "countries", key = "#countryId")
  public List<GeoCitiesResponse> getAddresses(Integer countryId) {
    log.info("Выбрана страна с countryId = " + countryId);
    return geoLauncher.getGeoCitiesResponseList().stream()
        .filter(a -> a.getCountryId().equals(countryId)).collect(
            Collectors.toList());
  }
}
