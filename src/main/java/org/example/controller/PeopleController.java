package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.PeopleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/people")
@RequiredArgsConstructor
@Slf4j
public class PeopleController {

  private final PeopleService peopleService;

  @GetMapping
  public ResponseEntity<?> getPeople(@RequestParam(defaultValue = "1") int page) {
    if (page < 1) {
      log.warn("Invalid page parameter: {}", page);
      return ResponseEntity.badRequest().body("Page number must be greater than or equal to 1");
    }

    return ResponseEntity.ok(peopleService.getPeople(page));
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getPersonById(@PathVariable int id) {
    if (id < 1) {
      log.warn("Invalid character id: {}", id);
      return ResponseEntity.badRequest().body("Character id must be greater than or equal to 1");
    }

    return ResponseEntity.ok(peopleService.getPersonDetails(id));
  }
}
