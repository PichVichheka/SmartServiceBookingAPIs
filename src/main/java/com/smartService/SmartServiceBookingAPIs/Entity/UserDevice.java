package com.smartService.SmartServiceBookingAPIs.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_device")
public class UserDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private Users user;

    @Column(nullable = false, name = "device_id")
    private String deviceId;

    private String browser;
    private String os;
    private String device;

    private String ipAddress;
    private LocalDateTime lastLogin;
    private boolean active = true;
}
