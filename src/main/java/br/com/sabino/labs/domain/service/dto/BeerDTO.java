package br.com.sabino.labs.domain.service.dto;

import br.com.sabino.labs.domain.entity.Beer;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link Beer} entity.
 */
public class BeerDTO implements Serializable {

    private String id;

    private String name;

    private String ibu;

    private String style;

    private String description;

    private String alcoholTenor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIbu() {
        return ibu;
    }

    public void setIbu(String ibu) {
        this.ibu = ibu;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAlcoholTenor() {
        return alcoholTenor;
    }

    public void setAlcoholTenor(String alcoholTenor) {
        this.alcoholTenor = alcoholTenor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BeerDTO)) {
            return false;
        }

        BeerDTO beerDTO = (BeerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, beerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BeerDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", ibu='" + getIbu() + "'" +
            ", style='" + getStyle() + "'" +
            ", description='" + getDescription() + "'" +
            ", alcoholTenor='" + getAlcoholTenor() + "'" +
            "}";
    }
}
