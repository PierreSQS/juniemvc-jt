package guru.springframework.juniemvc.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * DTO for BeerOrderLine entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class BeerOrderLineDto extends BaseEntityDto {
    
    private Integer beerId;
    private String beerName;

    // style of the beer, ALE, PALE ALE, IPA, etc.
    private String beerStyle;
    private String upc;
    
    @NotNull(message = "Order quantity is required")
    @Positive(message = "Order quantity must be positive")
    private Integer orderQuantity;
    
    @PositiveOrZero(message = "Quantity allocated must be zero or positive")
    private Integer quantityAllocated;
    
    private String status;
}