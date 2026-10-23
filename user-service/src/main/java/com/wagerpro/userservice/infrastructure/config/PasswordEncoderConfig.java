package com.wagerpro.userservice.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {

	// TODO Za vrednost 12, algoritam napravi $2^{12} = 4.096$ iteracija.
	//	To zapravo definiše koliko će procesor morati da se muči 
	//	(koliko je iteracija / računanja potrebno) da bi heširao lozinku.
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
