package com.smartService.SmartServiceBookingAPIs.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "provider_request")
public class ProviderRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne  // ✅ Link to the user who made the request
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private Users user;

    @Column(name = "service_type")
    private String serviceType;

    @Column(name = "service_description")
    private String serviceDescription;

    @Column(name = "experience")
    private String experience;

    @Enumerated(EnumType.STRING)  // ✅ Track request status
    @Column(name = "status")
    private RequestStatus status = RequestStatus.pending;
}
