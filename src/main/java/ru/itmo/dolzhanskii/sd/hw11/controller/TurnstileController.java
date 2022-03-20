package ru.itmo.dolzhanskii.sd.hw11.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.dolzhanskii.sd.hw11.facade.TurnstileFacade;

@RestController
@RequestMapping("/turnstile")
@RequiredArgsConstructor
public class TurnstileController {

    private final TurnstileFacade turnstileFacade;

    @PutMapping("/enter")
    public ResponseEntity<Object> validateAndMarkCustomerEntry(@RequestParam long passId) {
        HttpStatus status;
        if (turnstileFacade.validateAndMarkCustomerEntry(passId)) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.FORBIDDEN;
        }
        return new ResponseEntity<>(status);
    }

    @PutMapping("/exit")
    public ResponseEntity<Object> markCustomerExit(@RequestParam long passId) {
        return new ResponseEntity<>(turnstileFacade.markCustomerExit(passId) ? HttpStatus.OK : HttpStatus.FORBIDDEN);
    }
}
