package ru.itmo.dolzhanskii.sd.hw11.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.dolzhanskii.sd.hw11.model.dto.Report;
import ru.itmo.dolzhanskii.sd.hw11.util.ReportUtil;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportUtil reportUtil;

    @GetMapping
    public ResponseEntity<Report> getReport() {
        Report report = reportUtil.formNewReport();
        return new ResponseEntity<>(report, HttpStatus.OK);
    }
}
