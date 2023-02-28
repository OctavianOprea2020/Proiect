package com.fasttrackit.countriesapplication.service.scheduler;

import com.fasttrackit.countriesapplication.service.country.CountryService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@AllArgsConstructor
public class LotteryTask {
    private final CountryService countryService;

    @Scheduled(cron = "7 * * * * *")
    public void pickWinner() {
        Random random = new Random();
        int id = random.nextInt(countryService.getAllCountries().size());
        System.out.println("Winner country is: " + countryService.getById(id).getName());
    }
}
