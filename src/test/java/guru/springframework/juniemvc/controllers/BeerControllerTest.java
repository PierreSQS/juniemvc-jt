package guru.springframework.juniemvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.juniemvc.entities.Beer;
import guru.springframework.juniemvc.services.BeerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BeerControllerTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public BeerService beerService() {
            return mock(BeerService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BeerService beerService;

    @Autowired
    private ObjectMapper objectMapper;

    private Beer testBeer;
    private List<Beer> testBeers;

    @BeforeEach
    void setUp() {
        testBeer = Beer.builder()
                .id(1)
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456789")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        Beer testBeer2 = Beer.builder()
                .id(2)
                .beerName("Another Beer")
                .beerStyle("Lager")
                .upc("987654321")
                .price(new BigDecimal("9.99"))
                .quantityOnHand(200)
                .build();

        testBeers = Arrays.asList(testBeer, testBeer2);
    }

    @Test
    void getAllBeers() throws Exception {
        given(beerService.getAllBeers()).willReturn(testBeers);

        mockMvc.perform(get("/api/v1/beers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].beerName", is("Test Beer")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].beerName", is("Another Beer")));
    }

    @Test
    void getBeerById() throws Exception {
        given(beerService.getBeerById(1)).willReturn(Optional.of(testBeer));

        mockMvc.perform(get("/api/v1/beers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.beerName", is("Test Beer")))
                .andExpect(jsonPath("$.beerStyle", is("IPA")));
    }

    @Test
    void getBeerByIdNotFound() throws Exception {
        given(beerService.getBeerById(999)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/beers/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createBeer() throws Exception {
        Beer newBeer = Beer.builder()
                .beerName("New Beer")
                .beerStyle("Stout")
                .upc("111222333")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(50)
                .build();

        Beer savedBeer = Beer.builder()
                .id(3)
                .beerName("New Beer")
                .beerStyle("Stout")
                .upc("111222333")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(50)
                .build();

        given(beerService.saveBeer(any(Beer.class))).willReturn(savedBeer);

        mockMvc.perform(post("/api/v1/beers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newBeer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.beerName", is("New Beer")))
                .andExpect(jsonPath("$.beerStyle", is("Stout")));
    }

    @Test
    void updateBeer() throws Exception {
        Beer updatedBeer = Beer.builder()
                .beerName("Updated Beer")
                .beerStyle("Updated Style")
                .upc("999999")
                .price(new BigDecimal("19.99"))
                .quantityOnHand(150)
                .build();

        Beer savedBeer = Beer.builder()
                .id(1)
                .beerName("Updated Beer")
                .beerStyle("Updated Style")
                .upc("999999")
                .price(new BigDecimal("19.99"))
                .quantityOnHand(150)
                .build();

        // Use when/thenReturn instead of given/willReturn for more flexibility
        when(beerService.updateBeer(anyInt(), any(Beer.class))).thenReturn(Optional.of(savedBeer));

        System.out.println("[DEBUG_LOG] Test Beer JSON: " + objectMapper.writeValueAsString(updatedBeer));

        mockMvc.perform(put("/api/v1/beers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBeer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.beerName", is("Updated Beer")))
                .andExpect(jsonPath("$.beerStyle", is("Updated Style")));
    }

    @Test
    void updateBeerNotFound() throws Exception {
        Beer updatedBeer = Beer.builder()
                .beerName("Updated Beer")
                .beerStyle("Updated Style")
                .upc("999999")
                .price(new BigDecimal("19.99"))
                .quantityOnHand(150)
                .build();

        // Reset any previous mock setup
        reset(beerService);

        // Setup mock specifically for ID 999
        when(beerService.updateBeer(eq(999), any(Beer.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/beers/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBeer)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteBeer() throws Exception {
        given(beerService.deleteBeerById(1)).willReturn(true);

        mockMvc.perform(delete("/api/v1/beers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteBeerNotFound() throws Exception {
        given(beerService.deleteBeerById(999)).willReturn(false);

        mockMvc.perform(delete("/api/v1/beers/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
