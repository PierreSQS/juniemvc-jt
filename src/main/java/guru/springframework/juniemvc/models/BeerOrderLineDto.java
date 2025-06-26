package guru.springframework.juniemvc.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for BeerOrderLine entity
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeerOrderLineDto {
    private Integer id;
    private Integer version;
    private Integer orderId;
    private Integer beerId;
    private String beerName; // For convenience
    private Integer orderQuantity;
}