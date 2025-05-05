package com.system.card;

import com.system.card.auth.AuthenticationService;
import com.system.card.auth.RegisterRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static com.system.card.user.Role.ADMIN;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class CardManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardManagementApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service
	){
		return args -> {
			var admin = RegisterRequest.builder()
					.fullName("admin")
					.email("admin@mail.com")
					.password("pass")
					.role(ADMIN)
					.build();
			System.out.println("Admin token: " + service.register(admin).getAccessToken());
		};
	}
}
