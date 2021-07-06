package br.com.sabino.labs.domain.service;

import br.com.sabino.labs.domain.entity.Beer;
import br.com.sabino.labs.domain.repository.BeerRepository;
import br.com.sabino.labs.domain.service.dto.BeerDTO;
import br.com.sabino.labs.domain.service.mapper.BeerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Beer}.
 */
@Service
public class BeerService {

    private final Logger log = LoggerFactory.getLogger(BeerService.class);
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    public BeerService(BeerRepository beerRepository, BeerMapper beerMapper) {
        this.beerRepository = beerRepository;
        this.beerMapper = beerMapper;
    }

    /**
     * Save a beer.
     *
     * @param beerDTO the entity to save.
     * @return the persisted entity.
     */
    public BeerDTO save(BeerDTO beerDTO) {
        log.debug("Request to save Beer : {}", beerDTO);
        Beer beer = beerMapper.toEntity(beerDTO);
        beer = beerRepository.save(beer);
        return beerMapper.toDto(beer);
    }

    /**
     * Partially update a beer.
     *
     * @param beerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BeerDTO> partialUpdate(BeerDTO beerDTO) {
        log.debug("Request to partially update Beer : {}", beerDTO);

        return beerRepository
            .findById(beerDTO.getId())
            .map(
                existingBeer -> {
                    beerMapper.partialUpdate(existingBeer, beerDTO);

                    return existingBeer;
                }
            )
            .map(beerRepository::save)
            .map(beerMapper::toDto);
    }

    /**
     * Get all the beers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<BeerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Beers");
        return beerRepository.findAll(pageable).map(beerMapper::toDto);
    }

    /**
     * Get one beer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<BeerDTO> findOne(String id) {
        log.debug("Request to get Beer : {}", id);
        return beerRepository.findById(id).map(beerMapper::toDto);
    }

    /**
     * Delete the beer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Beer : {}", id);
        beerRepository.deleteById(id);
    }
}
