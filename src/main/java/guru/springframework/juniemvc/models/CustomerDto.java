package guru.springframework.juniemvc.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO for Customer entity
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private Integer id;
    private Integer version;
    private String name;
    private String email;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}