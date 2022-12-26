package ru.socialnet.team29;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.socialnet.team29.responses.GeoCitiesResponse;
import ru.socialnet.team29.responses.GeoCountriesResponse;

@RequiredArgsConstructor
@Component
@Getter
@Slf4j
public class GeoLauncher implements CommandLineRunner {

  private List<GeoCountriesResponse> countries;
  private List<GeoCitiesResponse> geoCitiesResponseList;
  private static Integer i;

  @Override
  public void run(String... args) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    List<Node> node = mapper.readValue(new URL("https://api.hh.ru/areas"), new TypeReference<>() {
    });
    List<GeoCitiesResponse> citiesLevel1 = node.stream()
        .flatMap(y -> y.getAreas().stream()).filter(y -> y.getAreas().isEmpty())
        .map(c -> new GeoCitiesResponse(c.getId(), c.getName(), c.getParent_id()))
        .collect(Collectors.toList());
    countries = node.stream().map(c -> new GeoCountriesResponse(c.getId(), c.getName())).collect(
        Collectors.toList());
    List<GeoCitiesResponse> citiesLevel2 = node.stream()
        .peek(q -> i = q.getId())
        .flatMap(y -> y.getAreas().stream()
            .flatMap(z -> z.getAreas().stream()
                .map(c -> new GeoCitiesResponse(c.getId(), c.getName(), i))))
        .collect(Collectors.toList());
    geoCitiesResponseList = Stream.concat(citiesLevel1.stream(),
            citiesLevel2.stream())
        .collect(Collectors.toList());
    log.info("Получены списки городов и стран");
  }

  @Data
  @NoArgsConstructor
  private static class Node {

    private Integer id;
    private Integer parent_id;
    private String name;
    private List<Node> areas;

    public Node(Integer id, Integer parent_id, String name) {
      this.id = id;
      this.parent_id = parent_id;
      this.name = name;
    }
  }
}

