package br.com.sabino.lab.api.rest;

import br.com.sabino.lab.SabinoLabsApp;
import br.com.sabino.lab.domain.entity.Beer;
import br.com.sabino.lab.domain.repository.BeerRepository;
import br.com.sabino.lab.domain.service.BeerService;
import br.com.sabino.lab.domain.service.dto.BeerDTO;
import br.com.sabino.lab.domain.service.mapper.BeerMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link BeerResource} REST controller.
 */
@SpringBootTest(classes = SabinoLabsApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class BeerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_IBU = "AAAAAAAAAA";
    private static final String UPDATED_IBU = "BBBBBBBBBB";

    private static final String DEFAULT_STYLE = "AAAAAAAAAA";
    private static final String UPDATED_STYLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ALCOHOL_TENOR = "AAAAAAAAAA";
    private static final String UPDATED_ALCOHOL_TENOR = "BBBBBBBBBB";

    @Autowired
    private BeerRepository beerRepository;

    @Autowired
    private BeerMapper BeerMapper;

    @Autowired
    private BeerService BeerService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MockMvc restBeerMockMvc;

    private Beer Beer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Beer createEntity(EntityManager em) {
        Beer Beer = br.com.sabino.lab.domain.entity.Beer.builder()
            .name(DEFAULT_NAME)
            .ibu(DEFAULT_IBU)
            .style(DEFAULT_STYLE)
            .description(DEFAULT_DESCRIPTION)
            .alcoholTenor(DEFAULT_ALCOHOL_TENOR)
            .build();
        return Beer;
    }

    @BeforeEach
    public void initTest() {
        this.Beer = createEntity(entityManager);
    }

    @Test
    @Transactional
    public void createBeer() throws Exception {
        int databaseSizeBeforeCreate = beerRepository.findAll().size();

        // Create the Beer
        BeerDTO BeerDTO = BeerMapper.toDto(Beer);

        restBeerMockMvc.perform(post("/api/beer")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(BeerDTO)))
            .andExpect(status().isCreated());

        // Validate the Beer in the database
        List<Beer> BeerList = beerRepository.findAll();
        assertThat(BeerList).hasSize(databaseSizeBeforeCreate + 1);

        Beer testBeer = BeerList.get(BeerList.size() - 1);
        assertThat(testBeer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBeer.getIbu()).isEqualTo(DEFAULT_IBU);
        assertThat(testBeer.getStyle()).isEqualTo(DEFAULT_STYLE);
        assertThat(testBeer.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBeer.getAlcoholTenor()).isEqualTo(DEFAULT_ALCOHOL_TENOR);
    }

    @Test
    @Transactional
    public void createBeerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = beerRepository.findAll().size();

        // Create the Beer with an existing ID
        Beer.setId(1L);
        BeerDTO BeerDTO = BeerMapper.toDto(Beer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBeerMockMvc.perform(post("/api/beer")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(BeerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Beer in the database
        List<Beer> BeerList = beerRepository.findAll();
        assertThat(BeerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllBeers() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(Beer);

        // Get all the BeerList
        restBeerMockMvc.perform(get("/api/beer?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(Beer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].ibu").value(hasItem(DEFAULT_IBU)))
            .andExpect(jsonPath("$.[*].style").value(hasItem(DEFAULT_STYLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].alcoholTenor").value(hasItem(DEFAULT_ALCOHOL_TENOR)));
    }

    @Test
    @Transactional
    public void getBeer() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(Beer);

        // Get the Beer
        restBeerMockMvc.perform(get("/api/beer/{id}", Beer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(Beer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.ibu").value(DEFAULT_IBU))
            .andExpect(jsonPath("$.style").value(DEFAULT_STYLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.alcoholTenor").value(DEFAULT_ALCOHOL_TENOR));
    }
    @Test
    @Transactional
    public void getNonExistingBeer() throws Exception {
        // Get the Beer
        restBeerMockMvc.perform(get("/api/beer/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBeer() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(Beer);

        int databaseSizeBeforeUpdate = beerRepository.findAll().size();

        // Update the Beer
        Beer updatedBeer = beerRepository.findById(Beer.getId()).get();
        // Disconnect from session so that the updates on updatedBeer are not directly saved in db
        entityManager.detach(updatedBeer);
        updatedBeer = br.com.sabino.lab.domain.entity.Beer.builder()
            .name(UPDATED_NAME)
            .ibu(UPDATED_IBU)
            .style(UPDATED_STYLE)
            .description(UPDATED_DESCRIPTION)
            .alcoholTenor(UPDATED_ALCOHOL_TENOR)
            .build();
        BeerDTO BeerDTO = BeerMapper.toDto(updatedBeer);

        restBeerMockMvc.perform(put("/api/beer")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(BeerDTO)))
            .andExpect(status().isOk());

        // Validate the Beer in the database
        List<Beer> BeerList = beerRepository.findAll();
        assertThat(BeerList).hasSize(databaseSizeBeforeUpdate);
        Beer testBeer = BeerList.get(BeerList.size() - 1);
        assertThat(testBeer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBeer.getIbu()).isEqualTo(UPDATED_IBU);
        assertThat(testBeer.getStyle()).isEqualTo(UPDATED_STYLE);
        assertThat(testBeer.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBeer.getAlcoholTenor()).isEqualTo(UPDATED_ALCOHOL_TENOR);
    }

    @Test
    @Transactional
    public void updateNonExistingBeer() throws Exception {
        int databaseSizeBeforeUpdate = beerRepository.findAll().size();

        // Create the Beer
        BeerDTO BeerDTO = BeerMapper.toDto(Beer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBeerMockMvc.perform(put("/api/beer")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(BeerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Beer in the database
        List<Beer> BeerList = beerRepository.findAll();
        assertThat(BeerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBeer() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(Beer);

        int databaseSizeBeforeDelete = beerRepository.findAll().size();

        // Delete the Beer
        restBeerMockMvc.perform(delete("/api/beer/{id}", Beer.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Beer> BeerList = beerRepository.findAll();
        assertThat(BeerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
