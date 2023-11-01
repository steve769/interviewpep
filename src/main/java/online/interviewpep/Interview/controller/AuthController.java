package online.interviewpep.Interview.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import online.interviewpep.Interview.dto.*;
import online.interviewpep.Interview.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@Valid @RequestBody RegisterUserDto registerUserDto, BindingResult bindingResult){
       return authService.registerUser(registerUserDto, bindingResult);
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@Valid @RequestBody LoginUserDto loginUserDto, BindingResult bindingResult){
        return authService.loginUser(loginUserDto, bindingResult);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<Map<String, Object>> forgotPassword(@Valid @RequestBody ForgotPasswordDto forgotPasswordDto, BindingResult bindingResult){
        return authService.forgotPassword(forgotPasswordDto, bindingResult);
    }

    @PostMapping("/resetMyPassword")
    public ResponseEntity<Map<String, Object>> resetMyPassword(@RequestBody ResetMyPasswordDto resetMyPasswordDto, @RequestParam String userId){
        return authService.resetMyPassword(resetMyPasswordDto, userId);
    }
    @GetMapping("/confirmEmail/{token}")
    public ResponseEntity<Map<String, Object>> confirmEmail(@PathVariable String token){
        return authService.confirmEmail(token);
    }

    @PatchMapping("/giveAdminRights")
    public ResponseEntity<Map<String, Object>> giveAdminRights(@Valid @RequestBody GiveAdminRightsDto giveAdminRightsDto, BindingResult bindingResult){
        return authService.giveAdminRights(giveAdminRightsDto, bindingResult);
    }

    @PatchMapping("/givePosterRights")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Map<String, Object>> givePosterRights(@Valid @RequestBody GivePosterRightsDto givePosterRightsDto, BindingResult bindingResult){
        return authService.givePosterRights(givePosterRightsDto, bindingResult);
    }
}
