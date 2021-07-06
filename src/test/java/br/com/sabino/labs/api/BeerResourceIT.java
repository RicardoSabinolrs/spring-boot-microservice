package br.com.sabino.labs.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.sabino.labs.IntegrationTest;
import br.com.sabino.labs.domain.entity.Beer;
import br.com.sabino.labs.domain.repository.BeerRepository;
import br.com.sabino.labs.domain.service.dto.BeerDTO;
import br.com.sabino.labs.domain.service.mapper.BeerMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link BeerResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BeerResourceIT {

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

    private static final String ENTITY_API_URL = "/api/beers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private BeerRepository beerRepository;

    @Autowired
    private BeerMapper beerMapper;

    @Autowired
    private MockMvc restBeerMockMvc;

    private Beer beer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Beer createEntity() {
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
    public static Beer createUpdatedEntity() {
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
        beerRepository.deleteAll();
        beer = createEntity();
    }

    @Test
    void createBeer() throws Exception {
        int databaseSizeBeforeCreate = beerRepository.findAll().size();
        // Create the Beer
        BeerDTO beerDTO = beerMapper.toDto(beer);
        restBeerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(beerDTO)))
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
    void createBeerWithExistingId() throws Exception {
        // Create the Beer with an existing ID
        beer.setId("existing_id");
        BeerDTO beerDTO = beerMapper.toDto(beer);

        int databaseSizeBeforeCreate = beerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBeerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(beerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Beer in the database
        List<Beer> beerList = beerRepository.findAll();
        assertThat(beerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllBeers() throws Exception {
        // Initialize the database
        beerRepository.save(beer);

        // Get all the beerList
        restBeerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(beer.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].ibu").value(hasItem(DEFAULT_IBU)))
            .andExpect(jsonPath("$.[*].style").value(hasItem(DEFAULT_STYLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].alcoholTenor").value(hasItem(DEFAULT_ALCOHOL_TENOR)));
    }

    @Test
    void getBeer() throws Exception {
        // Initialize the database
        beerRepository.save(beer);

        // Get the beer
        restBeerMockMvc
            .perform(get(ENTITY_API_URL_ID, beer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(beer.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.ibu").value(DEFAULT_IBU))
            .andExpect(jsonPath("$.style").value(DEFAULT_STYLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.alcoholTenor").value(DEFAULT_ALCOHOL_TENOR));
    }

    @Test
    void getNonExistingBeer() throws Exception {
        // Get the beer
        restBeerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewBeer() throws Exception {
        // Initialize the database
        beerRepository.save(beer);

        int databaseSizeBeforeUpdate = beerRepository.findAll().size();

        // Update the beer
        Beer updatedBeer = beerRepository.findById(beer.getId()).get();
        updatedBeer
            .name(UPDATED_NAME)
            .ibu(UPDATED_IBU)
            .style(UPDATED_STYLE)
            .description(UPDATED_DESCRIPTION)
            .alcoholTenor(UPDATED_ALCOHOL_TENOR);
        BeerDTO beerDTO = beerMapper.toDto(updatedBeer);

        restBeerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, beerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(beerDTO))
            )
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
    void putNonExistingBeer() throws Exception {
        int databaseSizeBeforeUpdate = beerRepository.findAll().size();
        beer.setId(UUID.randomUUID().toString());

        // Create the Beer
        BeerDTO beerDTO = beerMapper.toDto(beer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBeerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, beerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(beerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Beer in the database
        List<Beer> beerList = beerRepository.findAll();
        assertThat(beerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchBeer() throws Exception {
        int databaseSizeBeforeUpdate = beerRepository.findAll().size();
        beer.setId(UUID.randomUUID().toString());

        // Create the Beer
        BeerDTO beerDTO = beerMapper.toDto(beer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(beerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Beer in the database
        List<Beer> beerList = beerRepository.findAll();
        assertThat(beerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamBeer() throws Exception {
        int databaseSizeBeforeUpdate = beerRepository.findAll().size();
        beer.setId(UUID.randomUUID().toString());

        // Create the Beer
        BeerDTO beerDTO = beerMapper.toDto(beer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(beerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Beer in the database
        List<Beer> beerList = beerRepository.findAll();
        assertThat(beerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateBeerWithPatch() throws Exception {
        // Initialize the database
        beerRepository.save(beer);

        int databaseSizeBeforeUpdate = beerRepository.findAll().size();

        // Update the beer using partial update
        Beer partialUpdatedBeer = new Beer();
        partialUpdatedBeer.setId(beer.getId());

        partialUpdatedBeer.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).alcoholTenor(UPDATED_ALCOHOL_TENOR);

        restBeerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBeer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBeer))
            )
            .andExpect(status().isOk());

        // Validate the Beer in the database
        List<Beer> beerList = beerRepository.findAll();
        assertThat(beerList).hasSize(databaseSizeBeforeUpdate);
        Beer testBeer = beerList.get(beerList.size() - 1);
        assertThat(testBeer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBeer.getIbu()).isEqualTo(DEFAULT_IBU);
        assertThat(testBeer.getStyle()).isEqualTo(DEFAULT_STYLE);
        assertThat(testBeer.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBeer.getAlcoholTenor()).isEqualTo(UPDATED_ALCOHOL_TENOR);
    }

    @Test
    void fullUpdateBeerWithPatch() throws Exception {
        // Initialize the database
        beerRepository.save(beer);

        int databaseSizeBeforeUpdate = beerRepository.findAll().size();

        // Update the beer using partial update
        Beer partialUpdatedBeer = new Beer();
        partialUpdatedBeer.setId(beer.getId());

        partialUpdatedBeer
            .name(UPDATED_NAME)
            .ibu(UPDATED_IBU)
            .style(UPDATED_STYLE)
            .description(UPDATED_DESCRIPTION)
            .alcoholTenor(UPDATED_ALCOHOL_TENOR);

        restBeerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBeer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBeer))
            )
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
    void patchNonExistingBeer() throws Exception {
        int databaseSizeBeforeUpdate = beerRepository.findAll().size();
        beer.setId(UUID.randomUUID().toString());

        // Create the Beer
        BeerDTO beerDTO = beerMapper.toDto(beer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBeerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, beerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(beerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Beer in the database
        List<Beer> beerList = beerRepository.findAll();
        assertThat(beerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchBeer() throws Exception {
        int databaseSizeBeforeUpdate = beerRepository.findAll().size();
        beer.setId(UUID.randomUUID().toString());

        // Create the Beer
        BeerDTO beerDTO = beerMapper.toDto(beer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(beerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Beer in the database
        List<Beer> beerList = beerRepository.findAll();
        assertThat(beerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamBeer() throws Exception {
        int databaseSizeBeforeUpdate = beerRepository.findAll().size();
        beer.setId(UUID.randomUUID().toString());

        // Create the Beer
        BeerDTO beerDTO = beerMapper.toDto(beer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(beerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Beer in the database
        List<Beer> beerList = beerRepository.findAll();
        assertThat(beerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteBeer() throws Exception {
        // Initialize the database
        beerRepository.save(beer);

        int databaseSizeBeforeDelete = beerRepository.findAll().size();

        // Delete the beer
        restBeerMockMvc
            .perform(delete(ENTITY_API_URL_ID, beer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Beer> beerList = beerRepository.findAll();
        assertThat(beerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
