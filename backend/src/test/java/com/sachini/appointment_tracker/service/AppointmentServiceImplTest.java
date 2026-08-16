package com.sachini.appointment_tracker.service;

import com.sachini.appointment_tracker.entity.Appointment;
import com.sachini.appointment_tracker.entity.AppointmentStatus;
import com.sachini.appointment_tracker.exception.AppointmentNotFoundException;
import com.sachini.appointment_tracker.repository.AppointmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {
    @Mock
    private AppointmentRepository repository;

    @InjectMocks
    private AppointmentServiceImpl service;

    @Test
    void findAll_returnAllAppointmentsFromRepository() {
        Appointment appointment = Appointment.builder()
                .patientName("John Silva")
                .doctorName("Dr. Perera")
                .status(AppointmentStatus.SCHEDULED)
                .build();

        when(repository.findAll()).thenReturn(List.of(appointment));

        List<Appointment> result = service.findAll();

        assertThat(result).containsExactly(appointment);
    }

    @Test
    void findById_returnsAppointment_whenFound() {
        Appointment appointment = Appointment.builder()
                .patientName("John Silva")
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(appointment));

        Optional<Appointment> result = service.findById(1L);

        assertThat(result).contains(appointment);
    }

    @Test
    void findById_returnsEmpty_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<Appointment> result = service.findById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void create_savesAndReturnsAppointment() {
        Appointment appointment = Appointment.builder()
                .patientName("Nimal Fernando")
                .status(AppointmentStatus.SCHEDULED)
                .build();
        when(repository.save(appointment)).thenReturn(appointment);

        Appointment result = service.create(appointment);

        assertThat(result).isEqualTo(appointment);
        verify(repository).save(appointment);
    }

    @Test
    void updateStatus_updatesAndSaves_whenAppointmentExists() {
        Appointment appointment = Appointment.builder()
                .patientName("Kamal Perera")
                .status(AppointmentStatus.SCHEDULED)
                .build();
        when(repository.findById(1L)).thenReturn(Optional.of(appointment));
        when(repository.save(appointment)).thenReturn(appointment);

        Appointment result = service.updateStatus(1L, AppointmentStatus.COMPLETED);

        assertThat(result.getStatus()).isEqualTo(AppointmentStatus.COMPLETED);
        verify(repository).save(appointment);
    }

    @Test
    void updateStatus_throws_whenAppointmentNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateStatus(99L, AppointmentStatus.COMPLETED))
                .isInstanceOf(AppointmentNotFoundException.class);
    }

    @Test
    void delete_callsRepositoryDeleteById() {
        service.delete(1L);
        verify(repository).deleteById(1L);
    }
}
