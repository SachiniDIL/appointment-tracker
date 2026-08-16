package com.sachini.appointment_tracker.controller;

import com.sachini.appointment_tracker.dto.AppointmentRequest;
import com.sachini.appointment_tracker.entity.Appointment;
import com.sachini.appointment_tracker.entity.AppointmentStatus;
import com.sachini.appointment_tracker.exception.AppointmentNotFoundException;
import com.sachini.appointment_tracker.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppointmentRestController.class)
public class AppointmentRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AppointmentService service;

    @Test
    void getAllAppointments_returnsOkWithList() throws Exception {
        Appointment appointment = Appointment.builder()
                .patientName("John Silva")
                .doctorName("Dr. Perera")
                .appointmentDate(LocalDateTime.now().plusDays(1))
                .status(AppointmentStatus.SCHEDULED)
                .build();

        when(service.findAll()).thenReturn(List.of(appointment));

        mockMvc.perform(get("/api/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].patientName").value("John Silva"));
    }

    @Test
    void getAppointment_returnsNotFound_whenMissing() throws Exception {
        when(service.findById(999L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/appointments/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createAppointment_returnsCreated_whenValid() throws Exception {
        AppointmentRequest request = new AppointmentRequest();
        request.setPatientName("Jane Doe");
        request.setDoctorName("Dr. Smith");
        request.setAppointmentDate(LocalDateTime.now().plusDays(2));

        Appointment createdAppointment = Appointment.builder()
                .patientName(request.getPatientName())
                .doctorName(request.getDoctorName())
                .appointmentDate(request.getAppointmentDate())
                .status(AppointmentStatus.SCHEDULED)
                .build();
        createdAppointment.setId(1L);
        when(service.create(any(Appointment.class))).thenReturn(createdAppointment);

        mockMvc.perform(post("/api/appointments")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.patientName").value("Jane Doe"));
    }

    @Test
    void createAppointment_returnsBadRequest_whenInvalid() throws Exception {
        AppointmentRequest request = new AppointmentRequest();

        mockMvc.perform(post("/api/appointments")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.patientName").exists());
    }

    @Test
    void deleteAppointment_returnsNotFound_whenMissing() throws Exception {
        doThrow(new AppointmentNotFoundException(999L)).when(service).delete(999L);
        mockMvc.perform(get("/api/appointments/999"))
                .andExpect(status().isNotFound());
    }
}
