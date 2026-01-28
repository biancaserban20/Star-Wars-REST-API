package org.example.dto;

import java.util.List;

public record SwapiPageResponse(
    int count, String next, String previous, List<SwapiCharacter> results) {}
