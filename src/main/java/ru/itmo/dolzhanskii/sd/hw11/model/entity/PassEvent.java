package ru.itmo.dolzhanskii.sd.hw11.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "pass_events")
public class PassEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "time")
    private LocalDateTime time;

    @Column(name = "pass_id")
    private long passId;

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    private PassEventType type;

    @Column(name = "valid_until")
    private LocalDate validUntil;
}
