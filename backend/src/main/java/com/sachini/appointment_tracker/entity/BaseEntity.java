package com.sachini.appointment_tracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Common fields shared by every entity in this app.
 *
 * @MappedSuperclass tells Hibernate: this class is not itself a table,
 * but any entity that extends it inherits these columns into its own table.
 * This is the inheritance piece of the OOP requirement — Appointment
 * (and any future entity) gets id/createdAt for free instead of
 * redeclaring them.
 */
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();
}