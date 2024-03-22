package com.askMeProject.repository;

import com.askMeProject.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId " +
            "AND ((a.startTime BETWEEN :startTime AND :endTime) OR " +
            "(a.endTime BETWEEN :startTime AND :endTime) OR " +
            "(:startTime BETWEEN a.startTime AND a.endTime) OR " +
            "(:endTime BETWEEN a.startTime AND a.endTime))")
    List<Appointment> findOverlappingAppointments(@Param("doctorId") Long doctorId,
                                                  @Param("startTime") LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime);

}
