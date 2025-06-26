package guru.springframework.juniemvc.repositories;

import guru.springframework.juniemvc.entities.BeerOrder;
import guru.springframework.juniemvc.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for BeerOrder entity
 */
@Repository
public interface BeerOrderRepository extends JpaRepository<BeerOrder, Integer> {
    /**
     * Find all beer orders for a specific customer
     * @param customer The customer to find orders for
     * @return List of beer orders for the customer
     */
    List<BeerOrder> findAllByCustomer(Customer customer);
}