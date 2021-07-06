package br.com.sabino.labs.domain.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;

class BeerMapperTest {

    private BeerMapper beerMapper;

    @BeforeEach
    public void setUp() {
        beerMapper = new BeerMapperImpl();
    }
}
