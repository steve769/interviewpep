package online.interviewpep.Interview.service;

import online.interviewpep.Interview.dto.DisableAccountDto;
import online.interviewpep.Interview.dto.UserPatchDto;
import online.interviewpep.Interview.security.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public interface UserService {
    ResponseEntity<Map<String, Object>> fetchAllUsersByType(Role userType);

    ResponseEntity<Map<String, Object>> deleteUserById(String userId);

    ResponseEntity<Map<String, Object>> updateUserDetails(String userId, UserPatchDto userPatchDto);

    ResponseEntity<Map<String, Object>> disableUserAccount(DisableAccountDto disableAccountDto);

    ResponseEntity<Map<String, Object>> fetchAllUsers();

    ResponseEntity<Map<String, Object>> updateMyUserDetails(UserPatchDto userPatchDto);
}
