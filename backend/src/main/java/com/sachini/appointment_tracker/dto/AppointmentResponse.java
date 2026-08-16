package com.sachini.appointment_tracker.dto;

import com.sachini.appointment_tracker.entity.Appointment;
import com.sachini.appointment_tracker.entity.AppointmentStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AppointmentResponse {
    private Long id;
    private String patientName;
    private String doctorName;
    private LocalDateTime appointmentDate;
    private AppointmentStatus status;
    private LocalDateTime createdAt;

    public static AppointmentResponse from(Appointment appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .patientName(appointment.getPatientName())
                .doctorName(appointment.getDoctorName())
                .appointmentDate(appointment.getAppointmentDate())
                .status(appointment.getStatus())
                .createdAt(appointment.getCreatedAt())
                .build();
    }
}
