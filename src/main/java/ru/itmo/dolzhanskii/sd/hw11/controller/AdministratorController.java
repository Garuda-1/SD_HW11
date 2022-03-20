package ru.itmo.dolzhanskii.sd.hw11.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.dolzhanskii.sd.hw11.facade.AdministratorFacade;

@RestController
@RequestMapping("/administrator")
@RequiredArgsConstructor
public class AdministratorController {

    private final AdministratorFacade administratorFacade;

    @PostMapping
    public Long createPass(@RequestParam long days) {
        return administratorFacade.createPass(days);
    }

    @PostMapping("/updateValidUntil")
    public void changePassValidUntilDate(@RequestParam long passId, @RequestParam String newValidUntil) {
        administratorFacade.changePassValidUntilDate(passId, newValidUntil);
    }

    @PostMapping("/revoke")
    public void revokePass(@RequestParam long passId) {
        administratorFacade.revokePass(passId);
    }
}
