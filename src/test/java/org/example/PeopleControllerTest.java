package org.example;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.example.client.SwapiClient;
import org.example.dto.SwapiPageResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class PeopleControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private SwapiClient swapiClient;

  @Test
  void getPeople_shouldUseCache() throws Exception {
    SwapiPageResponse response = new SwapiPageResponse(1, null, null, List.of());

    when(swapiClient.getPeople(1)).thenReturn(response);

    mockMvc.perform(get("/people?page=1")).andExpect(status().isOk());

    mockMvc.perform(get("/people?page=1")).andExpect(status().isOk());

    verify(swapiClient, times(1)).getPeople(1);
  }
}
