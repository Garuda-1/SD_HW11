package ru.itmo.dolzhanskii.sd.hw11.util;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ru.itmo.dolzhanskii.sd.hw11.model.dto.Pass;
import ru.itmo.dolzhanskii.sd.hw11.model.dto.Report;
import ru.itmo.dolzhanskii.sd.hw11.model.dto.ReportMetadata;
import ru.itmo.dolzhanskii.sd.hw11.model.entity.TurnstileEvent;
import ru.itmo.dolzhanskii.sd.hw11.service.PassService;
import ru.itmo.dolzhanskii.sd.hw11.service.TurnstileService;

@Component
@RequiredArgsConstructor
public class ReportUtil {

    private final Clock clock;
    private final PassService passService;
    private final TurnstileService turnstileService;

    @Nullable
    private LocalDateTime lastReportTime = LocalDateTime.of(0, 1, 1, 0, 0);
    private final ReportMetadata reportMetadata = new ReportMetadata();

    public Report formNewReport() {
        LocalDateTime now = LocalDateTime.now(clock);
        List<Pass> passes = passService.findAll();
        for (Pass pass : passes) {
            updateDataByPassId(lastReportTime, pass.getId());
        }
        lastReportTime = now;
        return new Report(
            now,
            reportMetadata.getDailyVisitFrequency(),
            reportMetadata.getDailyVisitFrequency().values().stream().mapToLong(x -> x).average().orElse(0),
            reportMetadata.getTotalVisitCount() == 0 ? 0 :
                (double) reportMetadata.getVisitTotalDurationMinutes() / reportMetadata.getTotalVisitCount()
        );
    }

    private void updateDataByPassId(LocalDateTime from, long passId) {
        long visitDurationMinutes = 0;
        int visitCount = 0;
        LocalDateTime entryTime = null;
        List<TurnstileEvent> events = turnstileService.findAllByPassIdOrderByTime(from, passId);
        for (TurnstileEvent event : events) {
            switch (event.getType()) {
                case ENTRY:
                    entryTime = event.getTime();
                    reportMetadata.getDailyVisitFrequency().merge(
                        entryTime.toLocalDate(),
                        1L,
                        (date, prevCount) -> prevCount + 1
                    );
                    break;
                case EXIT:
                    if (entryTime == null) {
                        break;
                    }
                    visitDurationMinutes += ChronoUnit.MINUTES.between(entryTime, event.getTime());
                    visitCount++;
                    entryTime = null;
                    break;
            }
        }
        reportMetadata.setVisitTotalDurationMinutes(
            reportMetadata.getVisitTotalDurationMinutes() + visitDurationMinutes
        );
        reportMetadata.setTotalVisitCount(reportMetadata.getTotalVisitCount() + visitCount);
    }
}
