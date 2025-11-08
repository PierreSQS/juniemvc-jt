package guru.springframework.juniemvc.repositories;

import guru.springframework.juniemvc.entities.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveBeer() {
        // Given
        Beer beer = Beer.builder()
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        // When
        Beer savedBeer = beerRepository.save(beer);

        // Then
        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }

    @Test
    void testGetBeerById() {
        // Given
        Beer beer = Beer.builder()
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();
        Beer savedBeer = beerRepository.save(beer);

        // When
        Optional<Beer> fetchedBeerOptional = beerRepository.findById(savedBeer.getId());

        // Then
        assertThat(fetchedBeerOptional).isPresent();
        Beer fetchedBeer = fetchedBeerOptional.get();
        assertThat(fetchedBeer.getBeerName()).isEqualTo("Test Beer");
    }

    @Test
    void testUpdateBeer() {
        // Given
        Beer beer = Beer.builder()
                .beerName("Original Name")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();
        Beer savedBeer = beerRepository.save(beer);

        // When
        savedBeer.setBeerName("Updated Name");
        Beer updatedBeer = beerRepository.save(savedBeer);

        // Then
        assertThat(updatedBeer.getBeerName()).isEqualTo("Updated Name");
    }

    @Test
    void testDeleteBeer() {
        // Given
        Beer beer = Beer.builder()
                .beerName("Delete Me")
                .beerStyle("Lager")
                .upc("654321")
                .price(new BigDecimal("9.99"))
                .quantityOnHand(50)
                .build();
        Beer savedBeer = beerRepository.save(beer);

        // When
        beerRepository.deleteById(savedBeer.getId());
        Optional<Beer> deletedBeer = beerRepository.findById(savedBeer.getId());

        // Then
        assertThat(deletedBeer).isEmpty();
    }

    @Test
    void testListBeers() {
        // Given
        beerRepository.deleteAll(); // Clear any existing data
        Beer beer1 = Beer.builder()
                .beerName("Beer 1")
                .beerStyle("IPA")
                .upc("111111")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(100)
                .build();
        Beer beer2 = Beer.builder()
                .beerName("Beer 2")
                .beerStyle("Stout")
                .upc("222222")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(200)
                .build();
        beerRepository.saveAll(List.of(beer1, beer2));

        // When
        List<Beer> beers = beerRepository.findAll();

        // Then
        assertThat(beers).hasSize(2);
    }

    @Test
    void testFindAllPagedAndFiltered() {
        // Given
        beerRepository.deleteAll();
        for (int i = 1; i <= 15; i++) {
            beerRepository.save(Beer.builder()
                    .beerName("Test Beer " + i)
                    .beerStyle("Style")
                    .upc("UPC" + i)
                    .price(new BigDecimal("10.00"))
                    .quantityOnHand(10)
                    .build());
        }
        // Add a non-matching name
        beerRepository.save(Beer.builder()
                .beerName("Another Brand")
                .beerStyle("Style")
                .upc("UPC-XX")
                .price(new BigDecimal("11.00"))
                .quantityOnHand(5)
                .build());

        // When
        PageRequest pageRequest = PageRequest.of(0, 5);
        Page<Beer> page = beerRepository.findAllByBeerNameContainingIgnoreCase("Test", pageRequest);

        // Then
        assertThat(page.getTotalElements()).isEqualTo(15);
        assertThat(page.getTotalPages()).isEqualTo(3);
        assertThat(page.getContent()).hasSize(5);

        // When - second page
        page = beerRepository.findAllByBeerNameContainingIgnoreCase("Test", PageRequest.of(1, 5));
        assertThat(page.getContent()).hasSize(5);

        // When - all with pageable
        page = beerRepository.findAll(PageRequest.of(0, 10));
        assertThat(page.getTotalElements()).isEqualTo(16);
        assertThat(page.getContent()).hasSize(10);
    }

    @Test
    void testFindAllPagedAndFilteredByStyle() {
        // Given
        beerRepository.deleteAll();
        for (int i = 1; i <= 12; i++) {
            beerRepository.save(Beer.builder()
                    .beerName("Beer " + i)
                    .beerStyle(i % 2 == 0 ? "IPA" : "Lager")
                    .upc("UPC" + i)
                    .price(new BigDecimal("10.00"))
                    .quantityOnHand(10)
                    .build());
        }

        // When
        Page<Beer> page = beerRepository.findAllByBeerStyleContainingIgnoreCase("IPA", PageRequest.of(0, 5));

        // Then
        assertThat(page.getTotalElements()).isEqualTo(6);
        assertThat(page.getContent()).hasSize(5);

        // Next page
        page = beerRepository.findAllByBeerStyleContainingIgnoreCase("IPA", PageRequest.of(1, 5));
        assertThat(page.getContent()).hasSize(1);
    }

    @Test
    void testFindAllPagedAndFilteredByNameAndStyle() {
        // Given
        beerRepository.deleteAll();
        beerRepository.save(Beer.builder().beerName("Alpha One").beerStyle("IPA").upc("1").price(new BigDecimal("10.00")).quantityOnHand(10).build());
        beerRepository.save(Beer.builder().beerName("Alpha Two").beerStyle("Lager").upc("2").price(new BigDecimal("10.00")).quantityOnHand(10).build());
        beerRepository.save(Beer.builder().beerName("Beta One").beerStyle("IPA").upc("3").price(new BigDecimal("10.00")).quantityOnHand(10).build());

        // When
        Page<Beer> page = beerRepository.findAllByBeerNameContainingIgnoreCaseAndBeerStyleContainingIgnoreCase("Alpha", "IPA", PageRequest.of(0, 10));

        // Then
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().getFirst().getBeerName()).isEqualTo("Alpha One");
        assertThat(page.getContent().getFirst().getBeerStyle()).isEqualTo("IPA");
    }
}