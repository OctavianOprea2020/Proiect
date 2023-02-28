package com.fasttrackit.countriesapplication.model.country;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class City {

    public City(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @OneToOne(mappedBy = "capital")
    @JsonIgnore
    private Country capitalOf;

    @ManyToOne
    @JsonIgnore
    private Country country;
}
