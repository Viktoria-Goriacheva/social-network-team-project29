package ru.socialnet.team29.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.responses.GeoCountriesResponse;
import ru.socialnet.team29.responses.GeoCitiesResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Data
@Slf4j
public class GeoService {
    private List<GeoCountriesResponse> countries = new ArrayList<>();

    {
        countries.add(new GeoCountriesResponse(0, "РФ"));
        countries.add(new GeoCountriesResponse(1, "Казахстан"));
    }

    private List<GeoCitiesResponse> citiesKazakhstan = new ArrayList<>();

    {
        citiesKazakhstan.add(new GeoCitiesResponse(1, "Алма-Ата", 1));
    }

    List<GeoCitiesResponse> citiesRussia = new ArrayList<>();

    {
        citiesRussia.add(new GeoCitiesResponse(0, "Спб", 1));
        citiesRussia.add(new GeoCitiesResponse(0, "Москва", 1));
    }

    private Map<Integer, List<GeoCitiesResponse>> addresses = new HashMap<>();

    {
        addresses.put(countries.get(0).getId(), citiesRussia);
        addresses.put(countries.get(1).getId(), citiesKazakhstan);
    }
}
