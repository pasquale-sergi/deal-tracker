package dev.pasq.deal_track;

import dev.pasq.deal_track.entity.ApplicationUser;
import dev.pasq.deal_track.entity.Role;
import dev.pasq.deal_track.repository.RoleRepository;
import dev.pasq.deal_track.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@EnableScheduling

public class DealTrackApplication {

	public static void main(String[] args) {
		SpringApplication.run(DealTrackApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder encoder){
		return args->{
			if(roleRepository.findByAuthority("ADMIN").isPresent()) return;
			dev.pasq.deal_track.entity.Role adminRole = roleRepository.save(new Role("ADMIN"));
			roleRepository.save(new dev.pasq.deal_track.entity.Role("USER"));

			Set<dev.pasq.deal_track.entity.Role> roles = new HashSet<>();
			roles.add(adminRole);
			ApplicationUser admin = new ApplicationUser(1L,"admin", "admin@gmail.com", encoder.encode("password"), roles, true, true);
			userRepository.save(admin);
		};
	}
}
