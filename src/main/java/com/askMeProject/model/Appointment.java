package com.askMeProject.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Appointment extends BaseEntity{
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;
    private String note;
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private User patient;
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private User doctor;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
