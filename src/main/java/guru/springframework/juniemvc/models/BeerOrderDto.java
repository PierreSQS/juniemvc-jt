package guru.springframework.juniemvc.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Set;

/**
 * DTO for BeerOrder entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class BeerOrderDto extends BaseEntityDto {

    //reference information from a customer
    private String customerRef;
    
    @NotNull(message = "Payment amount is required")
    @Positive(message = "Payment amount must be positive")
    private BigDecimal paymentAmount;

    // enum status of the order, NEW, PAID, CANCELLED, INPROCESS, COMPLETE.
    private String status;
    
    @NotEmpty(message = "Beer order must have at least one beer order line")
    @Valid
    private Set<BeerOrderLineDto> beerOrderLines;
}