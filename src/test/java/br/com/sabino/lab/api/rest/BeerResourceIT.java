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
    private BeerMapper beerMapper;

    @Autowired
    private BeerService beerService;

    @Autowired
    private BeerQueryService beerQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBeerMockMvc;

    private Beer beer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Beer createEntity(EntityManager em) {
        Beer beer = new Beer()
            .name(DEFAULT_NAME)
            .ibu(DEFAULT_IBU)
            .style(DEFAULT_STYLE)
            .description(DEFAULT_DESCRIPTION)
            .alcoholTenor(DEFAULT_ALCOHOL_TENOR);
        return beer;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Beer createUpdatedEntity(EntityManager em) {
        Beer beer = new Beer()
            .name(UPDATED_NAME)
            .ibu(UPDATED_IBU)
            .style(UPDATED_STYLE)
            .description(UPDATED_DESCRIPTION)
            .alcoholTenor(UPDATED_ALCOHOL_TENOR);
        return beer;
    }

    @BeforeEach
    public void initTest() {
        beer = createEntity(em);
    }

    @Test
    @Transactional
    public void createBeer() throws Exception {
        int databaseSizeBeforeCreate = beerRepository.findAll().size();
        // Create the Beer
        BeerDTO beerDTO = beerMapper.toDto(beer);
        restBeerMockMvc.perform(post("/api/beers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(beerDTO)))
            .andExpect(status().isCreated());

        // Validate the Beer in the database
        List<Beer> beerList = beerRepository.findAll();
        assertThat(beerList).hasSize(databaseSizeBeforeCreate + 1);
        Beer testBeer = beerList.get(beerList.size() - 1);
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
        beer.setId(1L);
        BeerDTO beerDTO = beerMapper.toDto(beer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBeerMockMvc.perform(post("/api/beers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(beerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Beer in the database
        List<Beer> beerList = beerRepository.findAll();
        assertThat(beerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllBeers() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(beer);

        // Get all the beerList
        restBeerMockMvc.perform(get("/api/beers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(beer.getId().intValue())))
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
        beerRepository.saveAndFlush(beer);

        // Get the beer
        restBeerMockMvc.perform(get("/api/beers/{id}", beer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(beer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.ibu").value(DEFAULT_IBU))
            .andExpect(jsonPath("$.style").value(DEFAULT_STYLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.alcoholTenor").value(DEFAULT_ALCOHOL_TENOR));
    }


    @Test
    @Transactional
    public void getBeersByIdFiltering() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(beer);

        Long id = beer.getId();

        defaultBeerShouldBeFound("id.equals=" + id);
        defaultBeerShouldNotBeFound("id.notEquals=" + id);

        defaultBeerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBeerShouldNotBeFound("id.greaterThan=" + id);

        defaultBeerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBeerShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllBeersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(beer);

        // Get all the beerList where name equals to DEFAULT_NAME
        defaultBeerShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the beerList where name equals to UPDATED_NAME
        defaultBeerShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllBeersByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(beer);

        // Get all the beerList where name not equals to DEFAULT_NAME
        defaultBeerShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the beerList where name not equals to UPDATED_NAME
        defaultBeerShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllBeersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(beer);

        // Get all the beerList where name in DEFAULT_NAME or UPDATED_NAME
        defaultBeerShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the beerList where name equals to UPDATED_NAME
        defaultBeerShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllBeersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(beer);

        // Get all the beerList where name is not null
        defaultBeerShouldBeFound("name.specified=true");

        // Get all the beerList where name is null
        defaultBeerShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllBeersByNameContainsSomething() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(beer);

        // Get all the beerList where name contains DEFAULT_NAME
        defaultBeerShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the beerList where name contains UPDATED_NAME
        defaultBeerShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllBeersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(beer);

        // Get all the beerList where name does not contain DEFAULT_NAME
        defaultBeerShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the beerList where name does not contain UPDATED_NAME
        defaultBeerShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllBeersByIbuIsEqualToSomething() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(beer);

        // Get all the beerList where ibu equals to DEFAULT_IBU
        defaultBeerShouldBeFound("ibu.equals=" + DEFAULT_IBU);

        // Get all the beerList where ibu equals to UPDATED_IBU
        defaultBeerShouldNotBeFound("ibu.equals=" + UPDATED_IBU);
    }

    @Test
    @Transactional
    public void getAllBeersByIbuIsNotEqualToSomething() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(beer);

        // Get all the beerList where ibu not equals to DEFAULT_IBU
        defaultBeerShouldNotBeFound("ibu.notEquals=" + DEFAULT_IBU);

        // Get all the beerList where ibu not equals to UPDATED_IBU
        defaultBeerShouldBeFound("ibu.notEquals=" + UPDATED_IBU);
    }

    @Test
    @Transactional
    public void getAllBeersByIbuIsInShouldWork() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(beer);

        // Get all the beerList where ibu in DEFAULT_IBU or UPDATED_IBU
        defaultBeerShouldBeFound("ibu.in=" + DEFAULT_IBU + "," + UPDATED_IBU);

        // Get all the beerList where ibu equals to UPDATED_IBU
        defaultBeerShouldNotBeFound("ibu.in=" + UPDATED_IBU);
    }

    @Test
    @Transactional
    public void getAllBeersByIbuIsNullOrNotNull() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(beer);

        // Get all the beerList where ibu is not null
        defaultBeerShouldBeFound("ibu.specified=true");

        // Get all the beerList where ibu is null
        defaultBeerShouldNotBeFound("ibu.specified=false");
    }
                @Test
    @Transactional
    public void getAllBeersByIbuContainsSomething() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(beer);

        // Get all the beerList where ibu contains DEFAULT_IBU
        defaultBeerShouldBeFound("ibu.contains=" + DEFAULT_IBU);

        // Get all the beerList where ibu contains UPDATED_IBU
        defaultBeerShouldNotBeFound("ibu.contains=" + UPDATED_IBU);
    }

    @Test
    @Transactional
    public void getAllBeersByIbuNotContainsSomething() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(beer);

        // Get all the beerList where ibu does not contain DEFAULT_IBU
        defaultBeerShouldNotBeFound("ibu.doesNotContain=" + DEFAULT_IBU);

        // Get all the beerList where ibu does not contain UPDATED_IBU
        defaultBeerShouldBeFound("ibu.doesNotContain=" + UPDATED_IBU);
    }


    @Test
    @Transactional
    public void getAllBeersByStyleIsEqualToSomething() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(beer);

        // Get all the beerList where style equals to DEFAULT_STYLE
        defaultBeerShouldBeFound("style.equals=" + DEFAULT_STYLE);

        // Get all the beerList where style equals to UPDATED_STYLE
        defaultBeerShouldNotBeFound("style.equals=" + UPDATED_STYLE);
    }

    @Test
    @Transactional
    public void getAllBeersByStyleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(beer);

        // Get all the beerList where style not equals to DEFAULT_STYLE
        defaultBeerShouldNotBeFound("style.notEquals=" + DEFAULT_STYLE);

        // Get all the beerList where style not equals to UPDATED_STYLE
        defaultBeerShouldBeFound("style.notEquals=" + UPDATED_STYLE);
    }

    @Test
    @Transactional
    public void getAllBeersByStyleIsInShouldWork() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(beer);

        // Get all the beerList where style in DEFAULT_STYLE or UPDATED_STYLE
        defaultBeerShouldBeFound("style.in=" + DEFAULT_STYLE + "," + UPDATED_STYLE);

        // Get all the beerList where style equals to UPDATED_STYLE
        defaultBeerShouldNotBeFound("style.in=" + UPDATED_STYLE);
    }

    @Test
    @Transactional
    public void getAllBeersByStyleIsNullOrNotNull() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(beer);

        // Get all the beerList where style is not null
        defaultBeerShouldBeFound("style.specified=true");

        // Get all the beerList where style is null
        defaultBeerShouldNotBeFound("style.specified=false");
    }
                @Test
    @Transactional
    public void getAllBeersByStyleContainsSomething() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(beer);

        // Get all the beerList where style contains DEFAULT_STYLE
        defaultBeerShouldBeFound("style.contains=" + DEFAULT_STYLE);

        // Get all the beerList where style contains UPDATED_STYLE
        defaultBeerShouldNotBeFound("style.contains=" + UPDATED_STYLE);
    }

    @Test
    @Transactional
    public void getAllBeersByStyleNotContainsSomething() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(beer);

        // Get all the beerList where style does not contain DEFAULT_STYLE
        defaultBeerShouldNotBeFound("style.doesNotContain=" + DEFAULT_STYLE);

        // Get all the beerList where style does not contain UPDATED_STYLE
        defaultBeerShouldBeFound("style.doesNotContain=" + UPDATED_STYLE);
    }


    @Test
    @Transactional
    public void getAllBeersByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(beer);

        // Get all the beerList where description equals to DEFAULT_DESCRIPTION
        defaultBeerShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the beerList where description equals to UPDATED_DESCRIPTION
        defaultBeerShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllBeersByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(beer);

        // Get all the beerList where description not equals to DEFAULT_DESCRIPTION
        defaultBeerShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the beerList where description not equals to UPDATED_DESCRIPTION
        defaultBeerShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllBeersByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(beer);

        // Get all the beerList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultBeerShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the beerList where description equals to UPDATED_DESCRIPTION
        defaultBeerShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllBeersByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(beer);

        // Get all the beerList where description is not null
        defaultBeerShouldBeFound("description.specified=true");

        // Get all the beerList where description is null
        defaultBeerShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllBeersByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(beer);

        // Get all the beerList where description contains DEFAULT_DESCRIPTION
        defaultBeerShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the beerList where description contains UPDATED_DESCRIPTION
        defaultBeerShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllBeersByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(beer);

        // Get all the beerList where description does not contain DEFAULT_DESCRIPTION
        defaultBeerShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the beerList where description does not contain UPDATED_DESCRIPTION
        defaultBeerShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllBeersByAlcoholTenorIsEqualToSomething() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(beer);

        // Get all the beerList where alcoholTenor equals to DEFAULT_ALCOHOL_TENOR
        defaultBeerShouldBeFound("alcoholTenor.equals=" + DEFAULT_ALCOHOL_TENOR);

        // Get all the beerList where alcoholTenor equals to UPDATED_ALCOHOL_TENOR
        defaultBeerShouldNotBeFound("alcoholTenor.equals=" + UPDATED_ALCOHOL_TENOR);
    }

    @Test
    @Transactional
    public void getAllBeersByAlcoholTenorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(beer);

        // Get all the beerList where alcoholTenor not equals to DEFAULT_ALCOHOL_TENOR
        defaultBeerShouldNotBeFound("alcoholTenor.notEquals=" + DEFAULT_ALCOHOL_TENOR);

        // Get all the beerList where alcoholTenor not equals to UPDATED_ALCOHOL_TENOR
        defaultBeerShouldBeFound("alcoholTenor.notEquals=" + UPDATED_ALCOHOL_TENOR);
    }

    @Test
    @Transactional
    public void getAllBeersByAlcoholTenorIsInShouldWork() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(beer);

        // Get all the beerList where alcoholTenor in DEFAULT_ALCOHOL_TENOR or UPDATED_ALCOHOL_TENOR
        defaultBeerShouldBeFound("alcoholTenor.in=" + DEFAULT_ALCOHOL_TENOR + "," + UPDATED_ALCOHOL_TENOR);

        // Get all the beerList where alcoholTenor equals to UPDATED_ALCOHOL_TENOR
        defaultBeerShouldNotBeFound("alcoholTenor.in=" + UPDATED_ALCOHOL_TENOR);
    }

    @Test
    @Transactional
    public void getAllBeersByAlcoholTenorIsNullOrNotNull() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(beer);

        // Get all the beerList where alcoholTenor is not null
        defaultBeerShouldBeFound("alcoholTenor.specified=true");

        // Get all the beerList where alcoholTenor is null
        defaultBeerShouldNotBeFound("alcoholTenor.specified=false");
    }
                @Test
    @Transactional
    public void getAllBeersByAlcoholTenorContainsSomething() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(beer);

        // Get all the beerList where alcoholTenor contains DEFAULT_ALCOHOL_TENOR
        defaultBeerShouldBeFound("alcoholTenor.contains=" + DEFAULT_ALCOHOL_TENOR);

        // Get all the beerList where alcoholTenor contains UPDATED_ALCOHOL_TENOR
        defaultBeerShouldNotBeFound("alcoholTenor.contains=" + UPDATED_ALCOHOL_TENOR);
    }

    @Test
    @Transactional
    public void getAllBeersByAlcoholTenorNotContainsSomething() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(beer);

        // Get all the beerList where alcoholTenor does not contain DEFAULT_ALCOHOL_TENOR
        defaultBeerShouldNotBeFound("alcoholTenor.doesNotContain=" + DEFAULT_ALCOHOL_TENOR);

        // Get all the beerList where alcoholTenor does not contain UPDATED_ALCOHOL_TENOR
        defaultBeerShouldBeFound("alcoholTenor.doesNotContain=" + UPDATED_ALCOHOL_TENOR);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBeerShouldBeFound(String filter) throws Exception {
        restBeerMockMvc.perform(get("/api/beers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(beer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].ibu").value(hasItem(DEFAULT_IBU)))
            .andExpect(jsonPath("$.[*].style").value(hasItem(DEFAULT_STYLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].alcoholTenor").value(hasItem(DEFAULT_ALCOHOL_TENOR)));

        // Check, that the count call also returns 1
        restBeerMockMvc.perform(get("/api/beers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBeerShouldNotBeFound(String filter) throws Exception {
        restBeerMockMvc.perform(get("/api/beers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBeerMockMvc.perform(get("/api/beers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingBeer() throws Exception {
        // Get the beer
        restBeerMockMvc.perform(get("/api/beers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBeer() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(beer);

        int databaseSizeBeforeUpdate = beerRepository.findAll().size();

        // Update the beer
        Beer updatedBeer = beerRepository.findById(beer.getId()).get();
        // Disconnect from session so that the updates on updatedBeer are not directly saved in db
        em.detach(updatedBeer);
        updatedBeer
            .name(UPDATED_NAME)
            .ibu(UPDATED_IBU)
            .style(UPDATED_STYLE)
            .description(UPDATED_DESCRIPTION)
            .alcoholTenor(UPDATED_ALCOHOL_TENOR);
        BeerDTO beerDTO = beerMapper.toDto(updatedBeer);

        restBeerMockMvc.perform(put("/api/beers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(beerDTO)))
            .andExpect(status().isOk());

        // Validate the Beer in the database
        List<Beer> beerList = beerRepository.findAll();
        assertThat(beerList).hasSize(databaseSizeBeforeUpdate);
        Beer testBeer = beerList.get(beerList.size() - 1);
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
        BeerDTO beerDTO = beerMapper.toDto(beer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBeerMockMvc.perform(put("/api/beers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(beerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Beer in the database
        List<Beer> beerList = beerRepository.findAll();
        assertThat(beerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBeer() throws Exception {
        // Initialize the database
        beerRepository.saveAndFlush(beer);

        int databaseSizeBeforeDelete = beerRepository.findAll().size();

        // Delete the beer
        restBeerMockMvc.perform(delete("/api/beers/{id}", beer.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Beer> beerList = beerRepository.findAll();
        assertThat(beerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
