package ru.itmo.dolzhanskii.sd.hw11.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itmo.dolzhanskii.sd.hw11.facade.AdministratorFacade;

@Controller
@RequestMapping("/administrator-interface")
@RequiredArgsConstructor
public class GymAdministratorController {

    private final AdministratorFacade administratorFacade;

    @GetMapping
    public String getAdministratorPage(ModelMap modelMap) {
        modelMap.addAttribute("passes", administratorFacade.findAll());
        return "index";
    }


}
