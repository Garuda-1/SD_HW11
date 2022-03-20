package ru.itmo.dolzhanskii.sd.hw11.facade;

import java.time.Clock;
import java.time.LocalDate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itmo.dolzhanskii.sd.hw11.model.entity.TurnstileEventType;
import ru.itmo.dolzhanskii.sd.hw11.service.PassService;
import ru.itmo.dolzhanskii.sd.hw11.service.TurnstileService;

@Service
@RequiredArgsConstructor
public class TurnstileFacade {

    private final PassService passService;
    private final TurnstileService turnstileService;
    private final Clock clock;

    public boolean validateAndMarkCustomerEntry(long passId) {
        LocalDate date = passService.getPassValidUntil(passId);
        if (turnstileService.mostRecentEventType(passId) != TurnstileEventType.EXIT) {
            return false;
        }
        if (date == null || LocalDate.now(clock).isAfter(date)) {
            return false;
        }
        turnstileService.markCustomerEntry(passId);
        return true;
    }

    public boolean markCustomerExit(long passId) {
        if (turnstileService.mostRecentEventType(passId) != TurnstileEventType.ENTRY) {
            return false;
        }
        turnstileService.markCustomerExit(passId);
        return true;
    }
}
