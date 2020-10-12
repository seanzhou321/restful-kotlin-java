package base.donor.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import base.donor.model.Donor;
import base.donor.service.DonorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * @AutoConfigureMockMvc tests the full stack from the controller down without starting the server
 * This is a relatively expensive integration test, which require instantiation of all the beans
 */

@SpringBootTest
@AutoConfigureMockMvc
class DonorControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private DonorRepository donorRepository;

  private ObjectMapper objectMapper = new ObjectMapper();

  private Donor testDonor = new Donor("test1", "test2");

  @Test
  void create() {
    try {
      mockMvc.perform(post("/donor")
      .content(objectMapper.writeValueAsString(new Donor("Jack", "Dean")))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
          .andDo(print()).andExpect(status().isCreated())
          .andExpect(content().string(containsString("Dean")));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void view() {
    try {
      donorRepository.save(testDonor);
      mockMvc.perform(get("/donor")).andDo(print())
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.[*].id").isNotEmpty());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void findById() {
    try {
      Donor donor = donorRepository.save(testDonor);
      mockMvc.perform(get("/donor/{id}", donor.getId())).andDo(print())
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.firstName").value(testDonor.getFirstName()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void update() {
    try {
      Donor donor = donorRepository.save(testDonor);
      donor.setFirstName("put1");
      mockMvc.perform(put("/donor")
          .content(objectMapper.writeValueAsString(donor))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
          .andDo(print()).andExpect(status().isCreated())
          .andExpect(jsonPath("$.firstName").value(donor.getFirstName()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void delete() {
    try {
      Donor donor = donorRepository.save(testDonor);
      mockMvc.perform(MockMvcRequestBuilders.delete("/donor/{id}", donor.getId()))
          .andDo(print()).andExpect(status().isOk());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}