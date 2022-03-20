package ru.itmo.dolzhanskii.sd.hw11.model.dto;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class ReportMetadata {

    Map<LocalDate, Long> dailyVisitFrequency = new HashMap<>();
    long visitTotalDurationMinutes = 0;
    long totalVisitCount = 0;
}
