package br.com.sabino.lab.domain.service.dto;

import br.com.sabino.lab.domain.entity.Beer;
import lombok.*;

import java.io.Serializable;

/**
 * A DTO for the {@link Beer} entity.
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BeerDTO implements Serializable {
    private Long id;
    private String name;
    private String ibu;
    private String style;
    private String description;
    private String alcoholTenor;
}
