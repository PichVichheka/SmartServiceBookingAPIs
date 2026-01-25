package com.smartService.SmartServiceBookingAPIs.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "provider_availability")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProviderAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK → users.id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private Users provider;

    @Column(name = "available_date", nullable = false)
    private LocalDate availableDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "is_available")
    private boolean available = true;

}
