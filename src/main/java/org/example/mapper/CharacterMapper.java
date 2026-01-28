package org.example.mapper;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import org.example.dto.CharacterResponse;
import org.example.dto.SwapiCharacter;
import org.springframework.stereotype.Component;

@Component
public class CharacterMapper {

  private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

  public CharacterResponse toDetailsResponse(SwapiCharacter swapi) {

    double heightMeters = parseDouble(swapi.height()) / 100.0;
    double massKg = parseDouble(swapi.mass());

    String dateAdded = OffsetDateTime.parse(swapi.created()).format(OUTPUT_FORMAT);

    return new CharacterResponse(
        swapi.name(), heightMeters, massKg, swapi.birth_year(), swapi.films().size(), dateAdded);
  }

  private double parseDouble(String value) {
    try {
      return Double.parseDouble(value);
    } catch (Exception e) {
      return 0.0;
    }
  }
}
