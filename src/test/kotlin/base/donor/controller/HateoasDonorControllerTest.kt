package base.donor.controller

import base.donor.assembler.DonorModelAssembler
import base.donor.model.Donor
import base.donor.service.DonorRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*

/**
 * @WebMvcTest(class) tests only the restfull web layer. It allows to inject the mocks of the dependent services to the api layer.
 * Specifying the classes of this test suite
 *
 * @Import imports the assembler bean into the HateoasDonorController to be tested).
 */
@WebMvcTest(HateoasDonorController::class)
@Import(DonorModelAssembler::class)
internal class HateoasDonorControllerTest(@Autowired val mockMvc: MockMvc, @MockBean val donorRepository: DonorRepository) {

    val objectMapper = ObjectMapper()
    val testDonor = Donor("hateoastest1", "hateoastest2")

    @Test
    fun create() {
        try {
            val donorIn = Donor("Jackhateos", "Deanhateoas")
            var donorSaved = donorIn.clone()
            donorSaved.id = 1
            Mockito.`when`(donorRepository.save(ArgumentMatchers.eq(donorIn))).thenReturn(donorSaved)
            mockMvc.perform(MockMvcRequestBuilders.post("/hateoas/donor")
                    .content(objectMapper.writeValueAsString(Donor("Jackhateos", "Deanhateoas")))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isCreated)
                    .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(donorIn.lastName)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$._links.self.href").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$._links.donors.href").exists())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Test
    fun view() {
        try {
            val donors: MutableList<Donor?> = ArrayList()
            for (i in 1..3) {
                var t = Donor("first$i", "last$i")
                t.id = i
                donors.add(t)
            }
            Mockito.`when`(donorRepository.findAll()).thenReturn(donors)
            mockMvc.perform(MockMvcRequestBuilders.get("/hateoas/donor")).andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk)
                    .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.donorList[*].id").isNotEmpty)
                    .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.donorList[*]._links.self").isNotEmpty)
                    .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.donorList[*]._links.donors").isNotEmpty)
                    .andExpect(MockMvcResultMatchers.jsonPath("$._links.self").isNotEmpty)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Test
    fun findById() {
        try {
            val donor = testDonor.clone()
            donor.id = 5
            Mockito.`when`(donorRepository.findByIdOrNull(ArgumentMatchers.eq(donor.id!!))).thenReturn(donor)
            mockMvc.perform(MockMvcRequestBuilders.get("/hateoas/donor/{id}", donor.id)).andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk)
                    .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(testDonor.firstName!!))
                    .andExpect(MockMvcResultMatchers.jsonPath("$._links.self").isNotEmpty)
                    .andExpect(MockMvcResultMatchers.jsonPath("$._links.donors").isNotEmpty)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Test
    fun update() {
        try {
            val donor = testDonor.clone()
            donor.id = 3
            Mockito.`when`(donorRepository.save(ArgumentMatchers.eq(donor))).thenReturn(donor)
            mockMvc.perform(MockMvcRequestBuilders.put("/hateoas/donor")
                    .content(objectMapper.writeValueAsString(donor))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isCreated)
                    .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(testDonor.firstName!!))
                    .andExpect(MockMvcResultMatchers.jsonPath("$._links.self").isNotEmpty)
                    .andExpect(MockMvcResultMatchers.jsonPath("$._links.donors").isNotEmpty)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Test
    fun delete() {
        try {
            val donor = testDonor.clone()
            donor.id = 1
            mockMvc.perform(MockMvcRequestBuilders.delete("/hateoas/donor/{id}", donor.id))
                    .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}