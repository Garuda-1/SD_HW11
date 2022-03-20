package ru.itmo.dolzhanskii.sd.hw11.configuration;

import java.time.Clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

//    @Bean
//    public PassService passService(
//        PassEventRepository passEventRepository,
//        Clock clock
//    ) {
//        return new PassService(passEventRepository, clock);
//    }
//
//    @Bean
//    TurnstileService turnstileService(TurnstileEventRepository turnstileEventRepository) {
//        return new TurnstileService(turnstileEventRepository);
//    }
//
//    @Bean
//    AdministratorFacade administratorFacade(PassService passService) {
//        return new AdministratorFacade(passService);
//    }
//
//    @Bean
//    TurnstileFacade turnstileFacade(
//        PassService passService,
//        TurnstileService turnstileService
//    ) {
//        return new TurnstileFacade(passService, turnstileService);
//    }
//
//    @Bean
//    AdministratorController administratorController(AdministratorFacade administratorFacade) {
//        return new AdministratorController(administratorFacade);
//    }
}
