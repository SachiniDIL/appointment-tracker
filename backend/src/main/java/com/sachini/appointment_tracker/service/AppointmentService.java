package com.sachini.appointment_tracker.service;

import com.sachini.appointment_tracker.entity.Appointment;
import com.sachini.appointment_tracker.entity.AppointmentStatus;

import java.util.List;
import java.util.Optional;

public interface AppointmentService {

    List<Appointment> findAll();
    Optional<Appointment> findById(Long id);
    Appointment create(Appointment appointment);
    Appointment updateStatus(Long id, AppointmentStatus status);
    void delete(Long id);
}
