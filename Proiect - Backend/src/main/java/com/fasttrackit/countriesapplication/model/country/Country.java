package com.fasttrackit.countriesapplication.model.country;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "something_country")
public class Country {
    //name|capital|population|area|continent|neighbour1~neighbour2
    public Country(Long id, String name, City capital, long population, int area, String continent, List<String> neighbours, String code) {
        this.id = id;
        this.name = name;
        this.capital = capital;
        this.population = population;
        this.area = area;
        this.continent = continent;
        this.neighbours = neighbours;
        this.code = code;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "country_name", unique = true, columnDefinition = "varchar(2000)")
    private String name;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private City capital;
    @OneToMany(mappedBy = "country", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<City> cities;
    @Column
    private long population;
    @Column
    private int area;
    @Column
    private String continent;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    private List<Country> neighboursCountries;

    @Transient
    private List<String> neighbours;

    @Transient
    private List<String> distance;

    @Column
    private String code;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            System.out.println("(101) Return false"); // Octavian
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            System.out.println("(102) Return false"); // Octavian
            return false;
        }

        Country country = (Country) o;
        System.out.println("(103) id = " + id + ", country.id = " + country.id + ": " + (id == country.id)); // Octavian
        return id == country.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
