package org.example.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.CharacterResponse;
import org.example.dto.SwapiCharacter;
import org.example.mapper.CharacterMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/favourites")
@Slf4j
@RequiredArgsConstructor
public class FavouritesController {

  private final CharacterMapper characterMapper;

  @GetMapping
  public ResponseEntity<List<CharacterResponse>> getFavourites() {

    List<CharacterResponse> favourites =
        getStaticFavouriteCharacters().stream().map(characterMapper::toDetailsResponse).toList();

    log.info("Returning favourites for authenticated user");
    return ResponseEntity.ok(favourites);
  }

  private List<SwapiCharacter> getStaticFavouriteCharacters() {
    return List.of(
        new SwapiCharacter(
            "Luke Skywalker",
            "172",
            "77",
            "19BBY",
            List.of("film1", "film2", "film3", "film4"),
            "2014-12-09T13:50:51.644000Z"),
        new SwapiCharacter(
            "Darth Vader",
            "202",
            "136",
            "41.9BBY",
            List.of("film1", "film2", "film3", "film4", "film5"),
            "2014-12-09T13:50:51.644000Z"));
  }
}
