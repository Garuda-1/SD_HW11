package ru.itmo.dolzhanskii.sd.hw11.model.dto;

import java.time.LocalDate;

import lombok.Value;

@Value
public class Pass {

    long id;
    LocalDate validUntil;
    boolean revoked;
}
