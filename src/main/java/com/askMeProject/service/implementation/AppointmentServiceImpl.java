package com.askMeProject.service.implementation;

import com.askMeProject.common_constant.CommonConstant;
import com.askMeProject.dto.AppointmentDto;
import com.askMeProject.exception.BusinessException;
import com.askMeProject.exception.ErrorModel;
import com.askMeProject.model.Appointment;
import com.askMeProject.model.AppointmentStatus;
import com.askMeProject.model.User;
import com.askMeProject.repository.AppointmentRepository;
import com.askMeProject.repository.UserRepository;
import com.askMeProject.service.AppointmentService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {


    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final TaskScheduler taskScheduler;


    public String bookAppointment(AppointmentDto appointment) {
        // Find patient by email
        var existingPatient = findUserByEmail(appointment.getPatient().getEmail());

// Find doctor by email
        var existingDoctor = findUserByEmail(appointment.getDoctor().getEmail());


        // Check if the user initiating the appointment is a patient
        if (!existingPatient.getAuthorities().equals("PATIENT")) {
            return "Only patients can book appointments.";
        }

        // Check if the user being booked is a doctor
        if (!existingDoctor.getAuthorities().equals("DOCTOR")) {
            return "Only Doctor can book appointments.";
        }
        // Check doctor's availability
        if (!isDoctorAvailable(existingDoctor.getId(), appointment.getStartTime(), appointment.getEndTime())) {
            return "Doctor is not available at the specified time.";
        }
        var newAppointment = Appointment.builder()
                .patient(appointment.getPatient())
                .doctor(appointment.getDoctor())
                .note(appointment.getNote())
                .status(AppointmentStatus.SCHEDULED)
                .startTime(appointment.getStartTime())
                .endTime(appointment.getEndTime())
                .build();
        appointmentRepository.save(newAppointment);

        try {
            // Send initial appointment confirmation emails
            sendAppointmentConfirmation(newAppointment);

            // Schedule appointment reminder 5 minutes before the appointment
            scheduleAppointmentReminder(newAppointment);
        } catch (Exception e) {
            // Handle exception if email sending fails
            return "Appointment booked successfully, but failed to send reminders.";
        }


        return "Appointment booked successfully, by " + existingPatient.getFirstName() ;
    }

    public User findUserByEmail(String email) {
        Optional<User> findUser = userRepository.findByEmail(email);
        if (findUser.isEmpty()) {
            ErrorModel errorModel = ErrorModel.builder()
                    .code(CommonConstant.USER_NOT_FOUND_CODE)
                    .message(CommonConstant.USER_ALREADY_EXIST)
                    .build();
            throw new BusinessException(errorModel);
        }
        return findUser.get();
    }

    public void scheduleAppointmentReminder(Appointment appointment) {
        LocalDateTime reminderTime = appointment.getStartTime().minusMinutes(5);

        // Schedule task to send reminder 5 minutes before the appointment time
        taskScheduler.schedule(() -> {
            try {
                sendAppointmentReminder(appointment);
            } catch (Exception e) {
                // Handle exception if email sending fails
                System.out.println("Failed to send reminder for appointment ID: " + appointment.getId());
            }
        }, new CronTrigger("0 " + reminderTime.getMinute() + " " + reminderTime.getHour() + " " + reminderTime.getDayOfMonth() + " " + reminderTime.getMonthValue() + " ? " + reminderTime.getYear()));
    }

    private void sendAppointmentReminder(Appointment appointment) throws MessagingException, UnsupportedEncodingException, UnsupportedEncodingException {
        // Send appointment reminder to patient
        emailService.sendEmail(appointment.getPatient().getEmail(), "Appointment Reminder", "Your appointment is scheduled in 5 minutes.");

        // Send appointment reminder to doctor
        emailService.sendEmail(appointment.getDoctor().getEmail(), "Appointment Reminder", "You have an appointment scheduled in 5 minutes.");
    }

    public boolean isDoctorAvailable(Long doctorId, LocalDateTime startTime, LocalDateTime endTime) {

        List<Appointment> overlappingAppointments = appointmentRepository.findOverlappingAppointments(doctorId, startTime, endTime);

        return overlappingAppointments.isEmpty();
    }

    private void sendAppointmentConfirmation(Appointment appointment) throws MessagingException, UnsupportedEncodingException {
        // Send appointment confirmation to patient
        emailService.sendEmail(appointment.getPatient().getEmail(), "Appointment Confirmation", "Your appointment has been scheduled.");

        // Send appointment confirmation to doctor
        emailService.sendEmail(appointment.getDoctor().getEmail(), "Appointment Confirmation", "You have a new appointment scheduled.");
    }

    public String modifyAppointment(Long appointmentId, AppointmentDto modifiedAppointment) {
        // Check if the appointment exists
        Appointment existingAppointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() ->
                        new RuntimeException("Appointment not found."));

        // Check if the patient owns the appointment
        User patient = findUserByEmail(modifiedAppointment.getPatient().getEmail());
        if (!existingAppointment.getPatient().equals(patient)) {
            throw new RuntimeException("You are not authorized to modify this appointment.");
        }

        // Set the modified details
        existingAppointment.setStartTime(modifiedAppointment.getStartTime());
        existingAppointment.setEndTime(modifiedAppointment.getEndTime());
        existingAppointment.setNote(modifiedAppointment.getNote());
        // You can update other appointment details as needed

        // Save the modified appointment
        appointmentRepository.save(existingAppointment);
        return "Appointment updated successfully by " + patient.getFirstName();
    }
    public String cancelAppointment(Long appointmentId, String cancelerEmail) {
        // Fetch the appointment by ID
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found."));

        // Fetch the canceler by email
        User canceler = findUserByEmail(cancelerEmail);

        // Check if the canceler is authorized to cancel the appointment
        if (!isAuthorizedToCancel(appointment, canceler)) {
            throw new RuntimeException("You are not authorized to cancel this appointment.");
        }

        // Cancel the appointment
        appointmentRepository.delete(appointment);
        return "Appointment cancelled successfully by " + canceler.getFirstName();
    }
    private boolean isAuthorizedToCancel(Appointment appointment, User canceler) {
        return appointment.getPatient().equals(canceler);
    }

    public String completeAppointment(Long appointmentId, String doctorEmail) {
        // Fetch the appointment by ID
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found."));

        // Fetch the doctor by email
        User doctor = findUserByEmail(doctorEmail);

        // Check if the doctor is the one associated with the appointment
        if (!appointment.getDoctor().equals(doctor)) {
            throw new RuntimeException("You are not authorized to complete this appointment.");
        }

        // Mark the appointment as completed
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);
        return "Appointment completed successfully by " + doctor.getFirstName();
    }


}
