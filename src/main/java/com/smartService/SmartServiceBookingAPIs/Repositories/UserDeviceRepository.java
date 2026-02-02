package com.smartService.SmartServiceBookingAPIs.Repositories;

import com.smartService.SmartServiceBookingAPIs.Entity.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserDeviceRepository
        extends JpaRepository<UserDevice, Long> {

    Optional<UserDevice> findByUserIdAndDeviceId(
            Long userId,
            String deviceId
    );

    List<UserDevice> findAllByUserId(Long userId);
}
