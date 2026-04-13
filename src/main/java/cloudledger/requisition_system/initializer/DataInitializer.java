package cloudledger.requisition_system.initializer;

import cloudledger.requisition_system.models.dao.Role;
import cloudledger.requisition_system.models.dao.User;
import cloudledger.requisition_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if an ADMIN user already exists
        if (!userRepository.existsByRole(Role.ADMIN)) {
            // Create an admin user
            User admin = User.builder()
                    .firstname("Cloudledger")
                    .lastname("Administrator")
                    .email("admin@cloudledgerzim.com") // Default admin email
                    .password(passwordEncoder.encode("Maitashe#12")) // Default password
                    .role(Role.ADMIN)
                    .status("Active")
                    .build();

            userRepository.save(admin);
            System.out.println("Admin user created: admin@cloudledgerzim.com");
        } else {
            System.out.println("Admin user already exists. Skipping creation.");
        }
    }
}
