package br.com.sabino.labs.domain.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.sabino.labs.api.TestUtil;
import org.junit.jupiter.api.Test;

class BeerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BeerDTO.class);
        BeerDTO beerDTO1 = new BeerDTO();
        beerDTO1.setId("id1");
        BeerDTO beerDTO2 = new BeerDTO();
        assertThat(beerDTO1).isNotEqualTo(beerDTO2);
        beerDTO2.setId(beerDTO1.getId());
        assertThat(beerDTO1).isEqualTo(beerDTO2);
        beerDTO2.setId("id2");
        assertThat(beerDTO1).isNotEqualTo(beerDTO2);
        beerDTO1.setId(null);
        assertThat(beerDTO1).isNotEqualTo(beerDTO2);
    }
}
