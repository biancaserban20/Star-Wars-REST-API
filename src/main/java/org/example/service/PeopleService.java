package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.SwapiClient;
import org.example.dto.CharacterResponse;
import org.example.dto.SwapiPageResponse;
import org.example.mapper.CharacterMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

@Service
@RequiredArgsConstructor
@Slf4j
public class PeopleService {

  private final SwapiClient swapiClient;
  private final CharacterMapper characterMapper;

  @Cacheable(value = "people", key = "#page")
  public SwapiPageResponse getPeople(int page) {
    try {
      log.info("Cache miss for /people page={}, fetching from SWAPI", page);
      return swapiClient.getPeople(page);
    } catch (HttpClientErrorException.NotFound ex) {
      log.warn("Page {} not found in SWAPI", page);
      throw ex;
    } catch (ResourceAccessException ex) {
      log.error("SWAPI unavailable", ex);
      throw ex;
    }
  }

  public CharacterResponse getPersonDetails(int id) {
    try {
      return characterMapper.toDetailsResponse(swapiClient.getPersonById(id));
    } catch (HttpClientErrorException.NotFound ex) {
      log.warn("Character with id {} not found in SWAPI", id);
      throw ex;
    } catch (ResourceAccessException ex) {
      log.error("SWAPI unavailable while fetching character {}", id, ex);
      throw ex;
    }
  }
}
