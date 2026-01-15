package com.smartService.SmartServiceBookingAPIs.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fullname", nullable = false)
    private String fullname;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    // =====================
    // ROLES
    // =====================

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Roles> roles = new HashSet<>();


    // =====================
    // SERVICES (PROVIDER)
    // =====================

    // services.provider_id → users.id
    @OneToMany(mappedBy = "provider", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Services> services = new HashSet<>();


    // =====================
    // PROVIDER AVAILABILITY
    // =====================

    // provider_availability.provider_id → users.id
    @OneToMany(mappedBy = "provider", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<ProviderAvailability> availabilities = new HashSet<>();


    // =====================
    // BOOKINGS
    // =====================

    // booking.user_id → users.id
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Booking> bookingsAsUser = new HashSet<>();


    // booking.provider_id → users.id
    @OneToMany(mappedBy = "provider", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Booking> bookingsAsProvider = new HashSet<>();
}
