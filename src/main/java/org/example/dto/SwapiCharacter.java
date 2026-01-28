package org.example.dto;

import java.util.List;

public record SwapiCharacter(
    String name,
    String height,
    String mass,
    String birth_year,
    List<String> films,
    String created) {}
