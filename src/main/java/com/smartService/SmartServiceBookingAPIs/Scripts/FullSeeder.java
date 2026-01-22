package com.smartService.SmartServiceBookingAPIs.Scripts;

import com.smartService.SmartServiceBookingAPIs.Entity.Roles;
import com.smartService.SmartServiceBookingAPIs.Entity.Users;
import com.smartService.SmartServiceBookingAPIs.Repositories.RoleRepository;
import com.smartService.SmartServiceBookingAPIs.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.smartService.SmartServiceBookingAPIs.Exception.ErrorsExceptionFactory.notFound;

@Slf4j
@Component
@RequiredArgsConstructor
public class FullSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("Seeding");

        if (userRepository.findByUsername("provider").isEmpty()) {
            Roles providerRole = roleRepository.findByName("provider")
                    .orElseThrow(() -> notFound("Role not found."));


            Users provider = Users.builder()
                    .fullname("Provider kaka")
                    .username("servicekaka")
                    .email("provicer@gmail.com")
                    .password(passwordEncoder.encode("KaProvider21"))
                    .roles(Set.of(providerRole))
                    .build();

            userRepository.save(provider);
            log.info("seed completed");
        } else {
            log.info("Provider already exist");
        }
    }
}
