package com.smartService.SmartServiceBookingAPIs.Repositories;

import com.smartService.SmartServiceBookingAPIs.Entity.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;


//  UserDeviceRepository
//
//  This repository is responsible for all database operations
//  related to the UserDevice entity.
//
//  It automatically provides CRUD operations such as:
//  - save()
//  - findById()
//  - findAll()
//  - delete()
//
//  No implementation class is needed.
//  Spring Data JPA generates it at runtime.
public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {


     // Find a device record by:
     // - deviceId (generated hash of device info)
     // - userId (owner of the device)
     //
     // Spring Data JPA will automatically generate this query:
     //
     // SELECT d
     // FROM UserDevice d
     // WHERE d.deviceId = :deviceId
     //   AND d.user.id = :userId
     //
     // @param deviceId unique hash identifying the device
     // @param userId   ID of the user who owns the device
     // @return Optional<UserDevice>
     //         - contains UserDevice if found
     //         - empty if this device has never logged in before
    Optional<UserDevice> findByDeviceIdAndUserId(String deviceId, Long userId);
}
