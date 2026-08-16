package com.sachini.appointment_tracker.service;

import com.sachini.appointment_tracker.entity.Appointment;
import com.sachini.appointment_tracker.entity.AppointmentStatus;
import com.sachini.appointment_tracker.repository.AppointmentRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository repository;

    public AppointmentServiceImpl(AppointmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Appointment> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Appointment> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Appointment create(Appointment appointment) {
        return repository.save(appointment);
    }

    @Override
    public Appointment update(Long id, Appointment appointment) {
        Appointment existingAppointment = repository.findById(id).orElseThrow(NoSuchElementException::new);
        existingAppointment.setCreatedAt(appointment.getCreatedAt());
        existingAppointment.setDoctorName(appointment.getDoctorName());
        existingAppointment.setPatientName(appointment.getPatientName());

        return repository.save(existingAppointment);
    }

    @Override
    public Appointment updateStatus(Long id, AppointmentStatus status) {
        Appointment appointment = repository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Appointment with id " + id + " not found")
        );
        appointment.setStatus(status);
        return repository.save(appointment);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
