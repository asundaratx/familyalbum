package com.example.demo;

import com.example.demo.datamysql.dao.User;
import com.example.demo.datamysql.dao.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
@Slf4j
public class DemoApplication extends WebSecurityConfigurerAdapter {
	@Autowired
	UserRepository userRepository;
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http
				.authorizeRequests(a -> a
						.antMatchers("/", "/error", "/webjars/**").permitAll()
						.anyRequest().authenticated()
				)
				.logout(l -> l
						.logoutSuccessUrl("/").permitAll()
				)
				.csrf(c -> c
						.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
				)
				.exceptionHandling(e -> e
						.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
				)
				.oauth2Login();
		// @formatter:on
	}

	@GetMapping("/user")
	public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
		Map<String,Object> dataMap = new HashMap<>();
		String email = principal.getAttribute("email");
		dataMap.put("name", principal.getAttribute("name"));
		dataMap.put("email", principal.getAttribute("email"));
		if(email!=null) {
			User user = userRepository.findByEmail(email);
			if(user!=null) {
				log.info("This is an authorized user");
				dataMap.put("authorized", Boolean.TRUE);
			} else {
				dataMap.put("unauthorized", Boolean.TRUE);
			}
		}
		return dataMap;
	}
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
