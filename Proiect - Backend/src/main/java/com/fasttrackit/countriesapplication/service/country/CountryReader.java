package com.fasttrackit.countriesapplication.service.country;

import com.fasttrackit.countriesapplication.model.country.City;
import com.fasttrackit.countriesapplication.model.country.Country;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CountryReader {
    @Value("${file.countries}")
    private String fileCountriesPath;

    @Value("${max.countries:100}")
    private int maxCountries;

    public static int countryId = 0;

    public List<Country> getCountries() {
        try {
            // Octavian
            System.out.println("Citit!");
            return Files.lines(Path.of(fileCountriesPath))
                    .map(this::lineToCountry)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Country lineToCountry(String line) {
        String[] countryParts = line.split("\\|");
        // Octavian
        System.out.println("CountryID=" + countryId + ":"
                + countryParts[0] +"-"+ countryParts[1] +"-"+ countryParts[2] +"-"+ countryParts[3]
                +"-"+ countryParts[4] +"-"+ countryParts[5] +"-"+ countryParts[6]);
        return new Country(null, countryParts[0], new City(countryParts[1]), Long.parseLong(countryParts[2]),
                Integer.parseInt(countryParts[3]), countryParts[4],
                (countryParts.length > 5 ? parseNeighbours(countryParts[5]) : List.of()),
                countryParts[6]);
    }

    private List<String> parseNeighbours(String neighboursString) {
        return Arrays.stream(neighboursString.split("~")).toList();
    }

}
