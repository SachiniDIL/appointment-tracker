package com.sachini.appointment_tracker.repository;

import com.sachini.appointment_tracker.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}
