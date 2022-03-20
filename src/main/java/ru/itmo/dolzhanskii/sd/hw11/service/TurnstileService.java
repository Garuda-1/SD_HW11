package ru.itmo.dolzhanskii.sd.hw11.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itmo.dolzhanskii.sd.hw11.model.entity.TurnstileEvent;
import ru.itmo.dolzhanskii.sd.hw11.model.entity.TurnstileEventType;
import ru.itmo.dolzhanskii.sd.hw11.repository.TurnstileEventRepository;

@Service
@RequiredArgsConstructor
public class TurnstileService {

    private final TurnstileEventRepository turnstileEventRepository;
    private final Clock clock;

    public List<TurnstileEvent> findAllByPassIdOrderByTime(LocalDateTime from, long passId) {
        return turnstileEventRepository.findAllByPassIdOrderByTime(from, passId);
    }

    public TurnstileEventType mostRecentEventType(long passId) {
        return turnstileEventRepository.findMostRecentByPassId(passId).orElse(TurnstileEventType.EXIT);
    }

    public void markCustomerEntry(long passId) {
        saveTurnstileEvent(passId, TurnstileEventType.ENTRY);
    }

    public void markCustomerExit(long passId) {
        saveTurnstileEvent(passId, TurnstileEventType.EXIT);
    }

    private void saveTurnstileEvent(long passId, TurnstileEventType type) {
        TurnstileEvent turnstileEvent = new TurnstileEvent();
        turnstileEvent.setPassId(passId);
        turnstileEvent.setType(type);
        turnstileEvent.setTime(LocalDateTime.now(clock));
        turnstileEventRepository.save(turnstileEvent);
    }
}
