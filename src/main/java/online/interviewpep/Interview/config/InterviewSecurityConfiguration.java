package online.interviewpep.Interview.config;


import lombok.RequiredArgsConstructor;
import online.interviewpep.Interview.security.JwtFilter;
import online.interviewpep.Interview.security.UserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class InterviewSecurityConfiguration {
    private final JwtFilter jwtFilter;
    private final UserDetailsService userDetailsService;
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(8);
    }

    @Bean
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .userDetailsService(userDetailsService)
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/api/v1/auth/**")
                                .permitAll()
                                .requestMatchers("/api/v1/users/getAllUsers")
                                .hasRole("ADMIN")
                                .requestMatchers("/api/v1/users/fetchAllUsers/**")
                                .hasRole("ADMIN")
                                .requestMatchers("/api/v1/users/deleteUserById/**")
                                .hasRole("ADMIN")
                                .requestMatchers("/api/v1/users/updateUserDetails/**")
                                .hasRole("ADMIN")
                                .requestMatchers("/api/v1/users/updateMyUserDetails")
                                .hasAnyRole("ADMIN", "POSTER", "CANDIDATE")
                                .requestMatchers("/api/v1/listings/getAllListings")
                                .hasAnyRole("ADMIN", "POSTER", "CANDIDATE")
                                .requestMatchers("/api/v1/listings/jobListing/**")
                                .hasAnyRole("ADMIN", "POSTER", "CANDIDATE")
                                .requestMatchers("/api/v1/listings/createJobListing")
                                .hasAnyRole("ADMIN", "POSTER")
                                .requestMatchers("/api/v1/listings/applyForJob/**")
                                .hasRole("CANDIDATE")
                                .requestMatchers("/api/v1/listings/updateJobListing/**")
                                .hasAnyRole("ADMIN", "POSTER")
                                .requestMatchers("/api/v1/listings/deleteJobListing/**")
                                .hasAnyRole("ADMIN", "POSTER")
                                .requestMatchers("/api/v1/listings/getAllListingsByAverageSalary")
                                .hasAnyRole("ADMIN", "POSTER", "CANDIDATE")
                                .requestMatchers("/api/v1/listings/getAllListingsPostedByMe")
                                .hasAnyRole("ADMIN", "POSTER")
                                .requestMatchers("/api/v1/listings/getAllListingsByCategory")
                                .hasAnyRole("ADMIN", "POSTER", "CANDIDATE")
                                .requestMatchers("/api/v1/listings/fetchJobListingByDeadline")
                                .hasAnyRole("ADMIN", "POSTER", "CANDIDATE")
                                .anyRequest()
                                .authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))
                .httpBasic(it -> {})
                .addFilterBefore(jwtFilter, AuthorizationFilter.class);
        return http.build();
    }




}
