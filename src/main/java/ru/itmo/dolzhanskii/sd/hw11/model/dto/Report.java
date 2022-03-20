package ru.itmo.dolzhanskii.sd.hw11.model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import lombok.Value;

@Value
public class Report {

    LocalDateTime formedAt;
    Map<LocalDate, Long> visitsByDay;
    double averageVisitsCount;
    double averageVisitsTimeMinutes;
}
