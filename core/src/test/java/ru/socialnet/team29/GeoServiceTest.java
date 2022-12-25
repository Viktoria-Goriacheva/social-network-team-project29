package ru.socialnet.team29;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.socialnet.team29.service.GeoService;

@SpringBootTest
@Slf4j
public class GeoServiceTest {
  @Autowired
  private GeoService geoService;

  @Test
  public void getCountries() {

    getAndPrint();
    getAndPrint();
    getAndPrint();
  }

  private void getAndPrint() {
    log.info("Страны найдены: {}", geoService.getCountries());
  }

  @Test
  public void getCities() {

    getAndPrintById(5);
    getAndPrintById(40);
    getAndPrintById(40);
    getAndPrintById(5);
  }

  private void getAndPrintById(Integer id) {
    log.info("Города найдены: {}", geoService.getAddresses(id));
  }
}
