package neumatica.security.segurity_service_neumatica.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;
import neumatica.security.segurity_service_neumatica.service.CustomUserDetailsService;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	
	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http

            .csrf(csrf -> csrf.disable())

            .cors(cors -> {})

            .sessionManagement(session ->
                    session.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS
                    )
            )

            .authorizeHttpRequests(auth -> auth
            		
            		/*.requestMatchers(
                            org.springframework.http.HttpMethod.OPTIONS,
                            "/**"
                    ).permitAll()*/

                    .requestMatchers(
                            "/api/auth/register",
                            "/api/auth/login",
                            "/api/auth/refresh",
                            "/api/auth/despertar"
                    ).permitAll()

                    .requestMatchers(
                            "/api/users",
                            "/api/users/**",
                            "/api/admin/**"
                    ).hasRole("ADMIN")

                    .anyRequest().authenticated()
            )
            
            .oauth2ResourceServer(oauth2 ->
	            oauth2.jwt(jwt ->
	                jwt.jwtAuthenticationConverter(
	                    jwtAuthenticationConverter()
	                )
	            )
	        );

            /*.oauth2ResourceServer(oauth2 ->
	            oauth2.jwt(jwt -> {})
	        );*/

        return http.build();
    }
	
	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {

	    JwtGrantedAuthoritiesConverter authoritiesConverter =
	            new JwtGrantedAuthoritiesConverter();

	    authoritiesConverter.setAuthoritiesClaimName("roles");
	    authoritiesConverter.setAuthorityPrefix("");

	    JwtAuthenticationConverter converter =
	            new JwtAuthenticationConverter();

	    converter.setJwtGrantedAuthoritiesConverter(
	            authoritiesConverter
	    );

	    return converter;
	}

    @Bean
    public PasswordEncoder passwordEncoder() {

        return PasswordEncoderFactories
                .createDelegatingPasswordEncoder();
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(this.userDetailsService);

        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }
}
