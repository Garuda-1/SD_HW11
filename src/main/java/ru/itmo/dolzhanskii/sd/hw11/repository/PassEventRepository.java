package ru.itmo.dolzhanskii.sd.hw11.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.itmo.dolzhanskii.sd.hw11.model.entity.PassEvent;

public interface PassEventRepository extends JpaRepository<PassEvent, Long> {

    List<PassEvent> findAllByPassIdOrderByTimeAsc(long passId);

    @Query(value = "SELECT MAX(pass_id) + 1 FROM pass_events", nativeQuery = true)
    Long findAvailablePassId();

    @Query(value = "SELECT DISTINCT(pass_id) FROM pass_events", nativeQuery = true)
    List<Long> findAllPassIds();
}
