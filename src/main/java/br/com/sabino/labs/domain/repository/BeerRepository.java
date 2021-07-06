package br.com.sabino.labs.domain.repository;

import br.com.sabino.labs.domain.entity.Beer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Beer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BeerRepository extends MongoRepository<Beer, String> {}
