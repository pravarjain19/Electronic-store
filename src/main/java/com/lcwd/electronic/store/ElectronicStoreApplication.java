package com.lcwd.electronic.store;

import com.lcwd.electronic.store.entites.Role;
import com.lcwd.electronic.store.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ElectronicStoreApplication implements CommandLineRunner {

	@Autowired
	private  RoleRepository roleRepository;

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

		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
}
