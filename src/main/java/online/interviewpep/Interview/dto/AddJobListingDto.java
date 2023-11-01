package online.interviewpep.Interview.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddJobListingDto {
    @NotNull(message = "Listing title is null")
    private String title;
    @NotNull(message = "Company name is be null")
    private String company;
    @NotNull(message = "Category is null")
    private String category;
    @NotNull(message = "About company is null")
    private String aboutCompany;
    @NotNull(message = "Job description is null")
    private String description;
    @NotNull(message = "Compensation is null")
    private String compensation;
    @NotNull(message = "Deadline is null")
    private String deadline;
}
