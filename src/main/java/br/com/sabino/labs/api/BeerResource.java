package br.com.sabino.labs.api;

import br.com.sabino.labs.api.errors.BadRequestAlertException;
import br.com.sabino.labs.domain.entity.Beer;
import br.com.sabino.labs.domain.repository.BeerRepository;
import br.com.sabino.labs.domain.service.BeerService;
import br.com.sabino.labs.domain.service.dto.BeerDTO;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

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

    private final BeerRepository beerRepository;

    public BeerResource(BeerService beerService, BeerRepository beerRepository) {
        this.beerService = beerService;
        this.beerRepository = beerRepository;
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
        return ResponseEntity
            .created(new URI("/api/beers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /beers/:id} : Updates an existing beer.
     *
     * @param id the id of the beerDTO to save.
     * @param beerDTO the beerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated beerDTO,
     * or with status {@code 400 (Bad Request)} if the beerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the beerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/beers/{id}")
    public ResponseEntity<BeerDTO> updateBeer(@PathVariable(value = "id", required = false) final String id, @RequestBody BeerDTO beerDTO)
        throws URISyntaxException {
        log.debug("REST request to update Beer : {}, {}", id, beerDTO);
        if (beerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, beerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!beerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BeerDTO result = beerService.save(beerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, beerDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /beers/:id} : Partial updates given fields of an existing beer, field will ignore if it is null
     *
     * @param id the id of the beerDTO to save.
     * @param beerDTO the beerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated beerDTO,
     * or with status {@code 400 (Bad Request)} if the beerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the beerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the beerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/beers/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<BeerDTO> partialUpdateBeer(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody BeerDTO beerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Beer partially : {}, {}", id, beerDTO);
        if (beerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, beerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!beerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BeerDTO> result = beerService.partialUpdate(beerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, beerDTO.getId())
        );
    }

    /**
     * {@code GET  /beers} : get all the beers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of beers in body.
     */
    @GetMapping("/beers")
    public ResponseEntity<List<BeerDTO>> getAllBeers(Pageable pageable) {
        log.debug("REST request to get a page of Beers");
        Page<BeerDTO> page = beerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /beers/:id} : get the "id" beer.
     *
     * @param id the id of the beerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the beerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/beers/{id}")
    public ResponseEntity<BeerDTO> getBeer(@PathVariable String id) {
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
    public ResponseEntity<Void> deleteBeer(@PathVariable String id) {
        log.debug("REST request to delete Beer : {}", id);
        beerService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
