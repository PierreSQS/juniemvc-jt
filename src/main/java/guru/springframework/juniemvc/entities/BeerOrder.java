package guru.springframework.juniemvc.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Entity representing a beer order in the system
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BeerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Version
    private Integer version;

    private String orderStatus;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "beerOrder", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<BeerOrderLine> orderLines = new java.util.HashSet<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;

    /**
     * Convenience method to add order line and maintain relationship
     * @param orderLine The order line to add
     */
    public void addOrderLine(BeerOrderLine orderLine) {
        orderLine.setBeerOrder(this);
        this.orderLines.add(orderLine);
    }
}