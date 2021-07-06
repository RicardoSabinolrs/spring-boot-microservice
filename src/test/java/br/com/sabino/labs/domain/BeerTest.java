package br.com.sabino.labs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.sabino.labs.api.TestUtil;
import br.com.sabino.labs.domain.entity.Beer;
import org.junit.jupiter.api.Test;

class BeerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Beer.class);
        Beer beer1 = new Beer();
        beer1.setId("id1");
        Beer beer2 = new Beer();
        beer2.setId(beer1.getId());
        assertThat(beer1).isEqualTo(beer2);
        beer2.setId("id2");
        assertThat(beer1).isNotEqualTo(beer2);
        beer1.setId(null);
        assertThat(beer1).isNotEqualTo(beer2);
    }
}
