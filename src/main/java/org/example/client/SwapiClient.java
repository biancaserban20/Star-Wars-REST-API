package org.example.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.SwapiCharacter;
import org.example.dto.SwapiPageResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
@RequiredArgsConstructor
public class SwapiClient {

  private static final String SWAPI_BASE_URL = "https://swapi.dev/api";

  private final RestTemplate restTemplate;

  public SwapiPageResponse getPeople(int page) {
    log.info("Calling SWAPI for page {}", page);
    String url = SWAPI_BASE_URL + "/people/?page=" + page;
    return restTemplate.getForObject(url, SwapiPageResponse.class);
  }

  public SwapiCharacter getPersonById(int id) {
    log.info("Calling SWAPI for character id {}", id);
    String url = SWAPI_BASE_URL + "/people/" + id + "/";
    return restTemplate.getForObject(url, SwapiCharacter.class);
  }
}
