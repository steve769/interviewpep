package online.interviewpep.Interview.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResetPasswordDto {
    @NotNull(message = "Old password is null")
    private String oldPassword;
    @NotNull(message = "New password is null")
    private String newPassword;
}
