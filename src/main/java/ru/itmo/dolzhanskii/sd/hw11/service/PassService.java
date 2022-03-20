package ru.itmo.dolzhanskii.sd.hw11.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.itmo.dolzhanskii.sd.hw11.model.dto.Pass;
import ru.itmo.dolzhanskii.sd.hw11.model.entity.PassEvent;
import ru.itmo.dolzhanskii.sd.hw11.model.entity.PassEventType;
import ru.itmo.dolzhanskii.sd.hw11.repository.PassEventRepository;

@Service
@RequiredArgsConstructor
public class PassService {

    private final PassEventRepository passEventRepository;
    private final Clock clock;

    public Long cretePass(long days) {
        Long passId = Optional.ofNullable(passEventRepository.findAvailablePassId()).orElse(0L);
        savePassEvent(passId, PassEventType.CREATE, Optional.of(LocalDate.now(clock).plusDays(days)));
        return passId;
    }

    public void changePassValidUntilDate(long passId, LocalDate newValidUntil) {
        savePassEvent(passId, PassEventType.CHANGE_VALID_UNTIL_DATE, Optional.of(newValidUntil));
    }

    public void revokePass(long passId) {
        savePassEvent(passId, PassEventType.REVOKE, Optional.empty());
    }

    @Nullable
    public LocalDate getPassValidUntil(long passId) {
        List<PassEvent> passEvents = passEventRepository.findAllByPassIdOrderByTimeAsc(passId);
        LocalDate validUntil = null;
        for (PassEvent passEvent : passEvents) {
            switch (passEvent.getType()) {
                case CREATE:
                case CHANGE_VALID_UNTIL_DATE:
                    validUntil = passEvent.getValidUntil();
                    break;
                case REVOKE:
                    validUntil = null;
                    break;
            }
        }
        return validUntil;
    }

    public List<Pass> findAll() {
        LocalDate today = LocalDate.now(clock);
        return passEventRepository.findAllPassIds()
            .stream()
            .map(passId -> {
                LocalDate validUntil = getPassValidUntil(passId);
                return new Pass(passId, validUntil, validUntil == null || today.isAfter(validUntil));
            })
            .sorted(Comparator.comparingLong(Pass::getId).reversed())
            .collect(Collectors.toList());
    }

    private void savePassEvent(long passId, PassEventType type, Optional<LocalDate> validUntil) {
        PassEvent passEvent = new PassEvent();
        passEvent.setPassId(passId);
        passEvent.setType(type);
        passEvent.setTime(LocalDateTime.now(clock));
        validUntil.ifPresent(passEvent::setValidUntil);
        passEventRepository.save(passEvent);
    }
}
