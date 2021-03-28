package br.com.sabino.lab.domain.entity;


import javax.persistence.*;

import java.io.Serializable;

/**
 * A Beer.
 */
@Entity
@Table(name = "beer")
public class Beer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "ibu")
    private String ibu;

    @Column(name = "style")
    private String style;

    @Column(name = "description")
    private String description;

    @Column(name = "alcohol_tenor")
    private String alcoholTenor;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Beer name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIbu() {
        return ibu;
    }

    public Beer ibu(String ibu) {
        this.ibu = ibu;
        return this;
    }

    public void setIbu(String ibu) {
        this.ibu = ibu;
    }

    public String getStyle() {
        return style;
    }

    public Beer style(String style) {
        this.style = style;
        return this;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getDescription() {
        return description;
    }

    public Beer description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAlcoholTenor() {
        return alcoholTenor;
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
        return 31;
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
