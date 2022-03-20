package ru.itmo.dolzhanskii.sd.hw11.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.itmo.dolzhanskii.sd.hw11.model.entity.TurnstileEvent;
import ru.itmo.dolzhanskii.sd.hw11.model.entity.TurnstileEventType;

public interface TurnstileEventRepository extends JpaRepository<TurnstileEvent, Long> {

    @Query(
        value = "SELECT type FROM turnstile_events TE WHERE TE.time = (SELECT MAX(S.time) FROM turnstile_events S WHERE S.pass_id = :passId)",
        nativeQuery = true
    )
    Optional<TurnstileEventType> findMostRecentByPassId(long passId);

    @Query(
        value = "SELECT * FROM turnstile_events TE WHERE TE.time > :from AND TE.pass_id = :passId",
        nativeQuery = true
    )
    List<TurnstileEvent> findAllByPassIdOrderByTime(LocalDateTime from, long passId);
}
