package online.interviewpep.Interview.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GiveAdminRightsDto {
    @NotNull(message = "Email is null")
    @Email(message ="Email is invalid")
    private String email;
}
