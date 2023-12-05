package online.interviewpep.Interview.entity;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import online.interviewpep.Interview.security.Role;

import java.util.ArrayList;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull(message = "Firstname cannot be null")
    private String firstName;
    @NotNull(message = "Lastname cannot be null")
    private String lastName;
    @Email(message = "Email format is invalid")
    @NotNull(message = "Email cannot be null")
    @Column(unique = true)
    private String email;
    @Size(min = 6)
    @NotNull(message = "Password cannot be null")
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "poster", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<JobListing> postedJobListings = new ArrayList<>();
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_job_application",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "job_listing_id")
    )
    private List<JobListing> appliedJobListings = new ArrayList<>();

    private boolean enabled;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "token_id")
    private VerificationToken token;


}
