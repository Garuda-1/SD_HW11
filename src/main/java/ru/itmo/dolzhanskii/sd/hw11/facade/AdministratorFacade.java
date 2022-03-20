package ru.itmo.dolzhanskii.sd.hw11.facade;

import java.time.LocalDate;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itmo.dolzhanskii.sd.hw11.model.dto.Pass;
import ru.itmo.dolzhanskii.sd.hw11.service.PassService;

@Service
@RequiredArgsConstructor
public class AdministratorFacade {

    private final PassService passService;

    public Long createPass(long days) {
        return passService.cretePass(days);
    }

    public void changePassValidUntilDate(long passId, String newValidUntil) {
        passService.changePassValidUntilDate(passId, LocalDate.parse(newValidUntil));
    }

    public void revokePass(long passId) {
        passService.revokePass(passId);
    }

    public List<Pass> findAll() {
        return passService.findAll();
    }
}
