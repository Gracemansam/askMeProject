package com.askMeProject.controller;

import com.askMeProject.dto.AppointmentDto;
import com.askMeProject.service.implementation.AppointmentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentServiceImpl appointmentService;


    @PreAuthorize("hasAuthority('PATIENT')")
    @PostMapping("/book-appointment")
    public ResponseEntity<String> bookAppointment(@RequestBody AppointmentDto appointment) {
        String result = appointmentService.bookAppointment(appointment);
        return ResponseEntity.ok(result);
    }
    @PreAuthorize("hasAuthority('PATIENT')")
    @PutMapping("/modify/{appointmentId}")
    public ResponseEntity<String> modifyAppointment(@PathVariable Long appointmentId, @RequestBody AppointmentDto modifiedAppointment) {
        String result = appointmentService.modifyAppointment(appointmentId, modifiedAppointment);
        return ResponseEntity.ok(result);
    }
    @PreAuthorize("hasAuthority('PATIENT')")
    @DeleteMapping("/cancel/{appointmentId}")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long appointmentId, @RequestParam String cancelerEmail) {
        String result = appointmentService.cancelAppointment(appointmentId, cancelerEmail);
        return ResponseEntity.ok(result);
    }
    @PreAuthorize("hasAuthority('DOCTOR')")
    @PostMapping("/complete/{appointmentId}")
    public ResponseEntity<String> completeAppointment(@PathVariable Long appointmentId, @RequestParam String doctorEmail) {
        String result = appointmentService.completeAppointment(appointmentId, doctorEmail);
        return ResponseEntity.ok(result);
    }
}
