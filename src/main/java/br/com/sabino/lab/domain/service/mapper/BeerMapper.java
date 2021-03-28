package br.com.sabino.lab.domain.service.mapper;


import br.com.sabino.lab.domain.entity.Beer;
import br.com.sabino.lab.domain.service.dto.BeerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Beer} and its DTO {@link BeerDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BeerMapper extends EntityMapper<BeerDTO, Beer> {

    default Beer fromId(Long id) {
        if (id == null) {
            return null;
        }
        Beer beer = new Beer();
        beer.setId(id);
        return beer;
    }
}
