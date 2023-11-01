package online.interviewpep.Interview.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserPatchDto {
    private String firstName;
    private String lastName;
    private String email;
}
