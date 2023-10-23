package com.lcwd.electronic.store;

import com.lcwd.electronic.store.controllers.UserController;
import com.lcwd.electronic.store.dtos.RoleDto;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entites.Role;
import com.lcwd.electronic.store.entites.User;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.repositories.RoleRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.*;

@Component
@SpringBootApplication
@EnableWebMvc
public class ElectronicStoreApplication implements CommandLineRunner {

	@Autowired
	private  RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;



	public  static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);


	}


	@Override
	public void run(String... args) throws Exception {
		try {
			String roleId = "admin_fhashuhwutrhbvaytrw";
			String normalId = "normal_tiuwbnxcmntqtbnjaqe";

			Role roleAdmin = Role.builder().roleId(roleId).roleName("ROLE_ADMIN").build();
			Role roleNormal = Role.builder().roleId(normalId).roleName("ROLE_NORMAL").build();

			roleRepository.save(roleAdmin);
			roleRepository.save(roleNormal);

			User user = null;

			 user = userRepository.findByEmail("pravarjain1@gmail.com").orElseThrow(() -> new ResourceNotFoundException("User not found"));





		}
		catch (Exception e){

		User	user = User.builder().email("pravarjain1@gmail.com")
					.name("pravar").userId(UUID.randomUUID().toString()).about("admin").password(passwordEncoder.encode("admin")).gender("male").build();
			Set<Role> roles = new HashSet<>();
			roles.add(roleRepository.findById("admin_fhashuhwutrhbvaytrw").get());
			user.setRoles(roles);
			userRepository.save(user);

		}
	}
}
