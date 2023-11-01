package online.interviewpep.Interview.service;


import online.interviewpep.Interview.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Map;

@Service
public interface AuthService {

    ResponseEntity<Map<String, Object>> registerUser(RegisterUserDto registerUserDto, BindingResult bindingResult);

    ResponseEntity<Map<String, Object>> loginUser(LoginUserDto loginUserDto,BindingResult bindingResult);

    ResponseEntity<Map<String, Object>> resetUserPassword(ResetPasswordDto resetPasswordDto, BindingResult bindingResult);

    ResponseEntity<Map<String, Object>> forgotPassword(ForgotPasswordDto forgotPasswordDto, BindingResult bindingResult);

    ResponseEntity<Map<String, Object>> confirmEmail(String token);

    ResponseEntity<Map<String, Object>> resetMyPassword(ResetMyPasswordDto resetMyPasswordDto,String userId);

    ResponseEntity<Map<String, Object>> giveAdminRights(GiveAdminRightsDto giveAdminRightsDto, BindingResult bindingResult);

    ResponseEntity<Map<String, Object>> givePosterRights(GivePosterRightsDto givePosterRightsDto, BindingResult bindingResult);
}
