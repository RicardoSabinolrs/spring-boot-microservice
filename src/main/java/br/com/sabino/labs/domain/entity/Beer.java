package br.com.sabino.labs.domain.entity;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Beer.
 */
@Document(collection = "beer")
public class Beer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("ibu")
    private String ibu;

    @Field("style")
    private String style;

    @Field("description")
    private String description;

    @Field("alcohol_tenor")
    private String alcoholTenor;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Beer id(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Beer name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIbu() {
        return this.ibu;
    }

    public Beer ibu(String ibu) {
        this.ibu = ibu;
        return this;
    }

    public void setIbu(String ibu) {
        this.ibu = ibu;
    }

    public String getStyle() {
        return this.style;
    }

    public Beer style(String style) {
        this.style = style;
        return this;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getDescription() {
        return this.description;
    }

    public Beer description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAlcoholTenor() {
        return this.alcoholTenor;
    }

    public Beer alcoholTenor(String alcoholTenor) {
        this.alcoholTenor = alcoholTenor;
        return this;
    }

    public void setAlcoholTenor(String alcoholTenor) {
        this.alcoholTenor = alcoholTenor;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Beer)) {
            return false;
        }
        return id != null && id.equals(((Beer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Beer{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", ibu='" + getIbu() + "'" +
            ", style='" + getStyle() + "'" +
            ", description='" + getDescription() + "'" +
            ", alcoholTenor='" + getAlcoholTenor() + "'" +
            "}";
    }
}
