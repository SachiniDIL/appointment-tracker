package com.sachini.appointment_tracker.controller;


import com.sachini.appointment_tracker.dto.AppointmentRequest;
import com.sachini.appointment_tracker.dto.AppointmentResponse;
import com.sachini.appointment_tracker.entity.Appointment;
import com.sachini.appointment_tracker.entity.AppointmentStatus;
import com.sachini.appointment_tracker.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentRestController {

    private final AppointmentService service;

    public AppointmentRestController(AppointmentService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AppointmentResponse>> getAllAppointments() {
        List<AppointmentResponse> responses =
                service.findAll().stream().map(AppointmentResponse::from).toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> getAppointment(@PathVariable Long id) {
        return service.findById(id)
                .map(AppointmentResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AppointmentResponse> createAppointment(@Valid @RequestBody AppointmentRequest request) {
        Appointment appointment = Appointment.builder()
                .patientName(request.getPatientName())
                .doctorName(request.getDoctorName())
                .appointmentDate(request.getAppointmentDate())
                .status(AppointmentStatus.SCHEDULED)
                .build();

        Appointment saved = service.create(appointment);

        URI location = URI.create("/api/appointments/" + saved.getId());
        return ResponseEntity.created(location).body(AppointmentResponse.from(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponse> updateAppointment(@PathVariable Long id, @Valid @RequestBody AppointmentRequest request) {
        Appointment appointment = Appointment.builder()
                .patientName(request.getPatientName())
                .doctorName(request.getDoctorName())
                .appointmentDate(request.getAppointmentDate())
                .build();

        Appointment updated = service.update(id, appointment);
        return ResponseEntity.ok(AppointmentResponse.from(updated));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AppointmentResponse> updateAppointmentStatus(@PathVariable Long id, @Valid @RequestBody AppointmentStatus status) {
        Appointment updated = service.updateStatus(id, status);
        return ResponseEntity.ok(AppointmentResponse.from(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppointmentResponse> deleteAppointment(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
