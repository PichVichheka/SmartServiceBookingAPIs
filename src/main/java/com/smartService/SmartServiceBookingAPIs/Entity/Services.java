package com.smartService.SmartServiceBookingAPIs.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.nio.file.FileStore;

@Entity
@Table(name = "services")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Services {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_type")
    private String serviceType;

    @Column(name = "service_description")
    private String serviceDescription;

    @Column(name = "service_offer")
    private String serviceOffer;

    @Column(name = "price",precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "price_unit")
    private String priceUnit;

    // FK → users.id

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private Users provider;
}
