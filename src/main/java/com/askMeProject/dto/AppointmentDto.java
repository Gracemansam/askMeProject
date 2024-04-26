package com.askMeProject.dto;

import com.askMeProject.model.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentDto {

    private String note;
    private String patientEmail;
    private String doctorEmail;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
