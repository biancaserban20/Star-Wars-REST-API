package org.example;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AuthFlowTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void favourites_shouldReturn401_whenNotAuthenticated() throws Exception {
    mockMvc.perform(get("/favourites")).andExpect(status().isUnauthorized());
  }

  @Test
  void favourites_shouldReturn200_whenAuthenticated() throws Exception {
    String loginResponse =
        mockMvc
            .perform(
                post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                                        {
                                          "user": "demo-user"
                                        }
                                        """))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    String accessToken = JsonPath.read(loginResponse, "$.accessToken");

    mockMvc
        .perform(get("/favourites").header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isOk());
  }
}
