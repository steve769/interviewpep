package online.interviewpep.Interview.entity;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class JobListing {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String company;
    private String category;
    @Lob
    private String aboutCompany;
    @Lob
    private String description;
    private String compensation;
    private LocalDateTime deadline;
    private LocalDateTime createdAt;
    private int averageCompensation;
    @ManyToOne
    @JoinColumn(name = "poster_id")
    //@JsonBackReference
    private User poster;
    @ManyToMany(mappedBy = "appliedJobListings")
    @JsonIgnore
    private List<User> applicants = new ArrayList<>();
}
