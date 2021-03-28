package br.com.sabino.lab.api.rest;

import br.com.sabino.lab.api.rest.errors.BadRequestAlertException;
import br.com.sabino.lab.domain.entity.Beer;
import br.com.sabino.lab.domain.service.BeerService;
import br.com.sabino.lab.domain.service.dto.BeerDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link Beer}.
 */
@RestController
@RequestMapping("/api")
public class BeerResource {

    private final Logger log = LoggerFactory.getLogger(BeerResource.class);

    private static final String ENTITY_NAME = "sabinoLabsBeer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BeerService beerService;

    private final BeerQueryService beerQueryService;

    public BeerResource(BeerService beerService, BeerQueryService beerQueryService) {
        this.beerService = beerService;
        this.beerQueryService = beerQueryService;
    }

    /**
     * {@code POST  /beers} : Create a new beer.
     *
     * @param beerDTO the beerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new beerDTO, or with status {@code 400 (Bad Request)} if the beer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/beers")
    public ResponseEntity<BeerDTO> createBeer(@RequestBody BeerDTO beerDTO) throws URISyntaxException {
        log.debug("REST request to save Beer : {}", beerDTO);
        if (beerDTO.getId() != null) {
            throw new BadRequestAlertException("A new beer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BeerDTO result = beerService.save(beerDTO);
        return ResponseEntity.created(new URI("/api/beers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /beers} : Updates an existing beer.
     *
     * @param beerDTO the beerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated beerDTO,
     * or with status {@code 400 (Bad Request)} if the beerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the beerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/beers")
    public ResponseEntity<BeerDTO> updateBeer(@RequestBody BeerDTO beerDTO) throws URISyntaxException {
        log.debug("REST request to update Beer : {}", beerDTO);
        if (beerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BeerDTO result = beerService.save(beerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, beerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /beers} : get all the beers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of beers in body.
     */
    @GetMapping("/beers")
    public ResponseEntity<List<BeerDTO>> getAllBeers(BeerCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Beers by criteria: {}", criteria);
        Page<BeerDTO> page = beerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /beers/count} : count all the beers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/beers/count")
    public ResponseEntity<Long> countBeers(BeerCriteria criteria) {
        log.debug("REST request to count Beers by criteria: {}", criteria);
        return ResponseEntity.ok().body(beerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /beers/:id} : get the "id" beer.
     *
     * @param id the id of the beerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the beerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/beers/{id}")
    public ResponseEntity<BeerDTO> getBeer(@PathVariable Long id) {
        log.debug("REST request to get Beer : {}", id);
        Optional<BeerDTO> beerDTO = beerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(beerDTO);
    }

    /**
     * {@code DELETE  /beers/:id} : delete the "id" beer.
     *
     * @param id the id of the beerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/beers/{id}")
    public ResponseEntity<Void> deleteBeer(@PathVariable Long id) {
        log.debug("REST request to delete Beer : {}", id);
        beerService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
