package br.com.sabino.labs.domain.service.mapper;

import br.com.sabino.labs.domain.entity.Beer;
import br.com.sabino.labs.domain.service.dto.BeerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Beer} and its DTO {@link BeerDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BeerMapper extends EntityMapper<BeerDTO, Beer> {}
