package online.interviewpep.Interview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JobListingDto {
    private String title;
    private String company;
    private String category;
    private String aboutCompany;
    private String description;
    private String compensation;
    private String deadline;
}
