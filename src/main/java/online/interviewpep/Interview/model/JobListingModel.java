package online.interviewpep.Interview.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JobListingModel {
    private Long id;
    private String title;
    private String company;
    private String category;
    private String description;
    private LocalDateTime deadline;
    private LocalDateTime createdAt;
    private int averageCompensation;
    private String postedBy;
}
