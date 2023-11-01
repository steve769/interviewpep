package online.interviewpep.Interview.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import online.interviewpep.Interview.dto.DisableAccountDto;
import online.interviewpep.Interview.dto.ResetPasswordDto;
import online.interviewpep.Interview.dto.UserPatchDto;
import online.interviewpep.Interview.security.Role;
import online.interviewpep.Interview.service.AuthService;
import online.interviewpep.Interview.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/fetchAllUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> fetchAllUsersByType(@RequestParam String userType){
        //userRole=all, posters, admins, candidates
        return userService.fetchAllUsersByType(Role.valueOf(userType.toUpperCase()));
    }
    @GetMapping("/getAllUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> fetchAllUsers(){
        //userRole=all, posters, admins, candidates
        return userService.fetchAllUsers();
    }
    @DeleteMapping("/deleteUserById/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> deleteUserById(@PathVariable String userId){
        return userService.deleteUserById(userId);
    }
    @PatchMapping("/updateUserDetails/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> updateUserDetails(@PathVariable String userId, @RequestBody UserPatchDto userPatchDto){
        return userService.updateUserDetails(userId,userPatchDto);
    }

    @PatchMapping("/updateMyUserDetails")
    @PreAuthorize("hasRole('ADMIN') or hasRole('POSTER') or hasRole('CANDIDATE')")
    public ResponseEntity<Map<String, Object>> updateMyUserDetails(@RequestBody UserPatchDto userPatchDto){
        return userService.updateMyUserDetails(userPatchDto);
    }
    @PostMapping("/disableUserAccount")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> disableUserAccount(@RequestBody DisableAccountDto disableAccountDto){
        return userService.disableUserAccount(disableAccountDto);
    }

    @PostMapping("/resetPassword")
    @PreAuthorize("hasRole('ADMIN') or hasRole('POSTER') or hasRole('CANDIDATE')")
    public ResponseEntity<Map<String, Object>> resetUserPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto, BindingResult bindingResult){
        return authService.resetUserPassword(resetPasswordDto, bindingResult);
    }
}
