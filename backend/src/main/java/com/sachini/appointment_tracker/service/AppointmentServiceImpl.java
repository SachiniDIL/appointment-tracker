package com.sachini.appointment_tracker.service;

import com.sachini.appointment_tracker.entity.Appointment;
import com.sachini.appointment_tracker.entity.AppointmentStatus;
import com.sachini.appointment_tracker.exception.AppointmentNotFoundException;
import com.sachini.appointment_tracker.repository.AppointmentRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
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
        Appointment existing =
                repository.findById(id).orElseThrow(() -> new AppointmentNotFoundException(id));
        existing.setPatientName(appointment.getPatientName());
        existing.setDoctorName(appointment.getDoctorName());
        existing.setAppointmentDate(appointment.getAppointmentDate());
        return repository.save(existing);
    }

    @Override
    public Appointment updateStatus(Long id, AppointmentStatus status) {
        Appointment appointment =
                repository.findById(id).orElseThrow(() -> new AppointmentNotFoundException(id));
        appointment.setStatus(status);
        return repository.save(appointment);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new AppointmentNotFoundException(id);
        }
        repository.deleteById(id);
    }
}