package br.com.sabino.lab.domain.entity;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A Beer.
 */
@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "beer")
public class Beer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Setter
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
}

