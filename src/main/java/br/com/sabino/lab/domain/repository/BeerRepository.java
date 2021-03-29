package br.com.sabino.lab.domain.repository;

import br.com.sabino.lab.domain.entity.Beer;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Beer entity.
 */
@Repository
public interface BeerRepository extends JpaRepository<Beer, Long> {
}
