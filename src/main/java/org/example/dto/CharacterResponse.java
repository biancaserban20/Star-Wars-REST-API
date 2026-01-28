package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CharacterResponse(
    String name,
    double height,
    double mass,
    @JsonProperty("birth_year") String birthYear,
    @JsonProperty("number_of_films") int numberOfFilms,
    @JsonProperty("date_added") String dateAdded) {}
