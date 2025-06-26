package guru.springframework.juniemvc.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for BeerOrder entity
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeerOrderDto {
    private Integer id;
    private Integer version;
    private String orderStatus;
    private Integer customerId;
    private String customerName; // For convenience
    private Set<BeerOrderLineDto> orderLines;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}