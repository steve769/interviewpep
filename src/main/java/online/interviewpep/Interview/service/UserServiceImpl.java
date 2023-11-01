package online.interviewpep.Interview.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.interviewpep.Interview.dto.DisableAccountDto;
import online.interviewpep.Interview.dto.UserPatchDto;
import online.interviewpep.Interview.entity.User;
import online.interviewpep.Interview.repository.UserRepository;
import online.interviewpep.Interview.security.Role;
import online.interviewpep.Interview.utility.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    @Override
    public ResponseEntity<Map<String, Object>> fetchAllUsersByType(Role userType) {
        try{
            log.info("Reached service layer");
            List<User> users = new ArrayList<>();

            if(userType.name().equalsIgnoreCase("candidate")){
                users = userRepository.findAllByRole(Role.CANDIDATE);
            }

            if(userType.name().equalsIgnoreCase("poster")){
                users = userRepository.findAllByRole(Role.POSTER);
            }

            if(userType.name().equalsIgnoreCase("admin")){
                users = userRepository.findAllByRole(Role.ADMIN);
            }

            Map<String, Object> userData = new HashMap<>();
            if(!users.isEmpty()){
                userData.put("count", users.size());
                userData.put("users", users);
            }else{
                userData.put("count", 0);
                userData.put("users", users);
            }

            return APIResponse.genericResponse("success", 11, userData, HttpStatus.OK);
        }catch(Exception ex){
            return APIResponse.genericResponse("failure", 12, ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> deleteUserById(String userId) {
        try{
            //Before deleting user find them in DB
            User user = userRepository.findById(Long.valueOf(userId)).orElse(null);

            if(user == null){
                return APIResponse.genericResponse("failure", 12, "User does not exist", HttpStatus.BAD_REQUEST);
            }
            userRepository.deleteById(Long.valueOf(userId));

            String msg = String.format("User with ID %s deleted successfully", userId);

            return APIResponse.genericResponse("success", 11, msg, HttpStatus.OK);
        }catch(Exception ex){
            return APIResponse.genericResponse("failure", 12, ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> updateUserDetails(String userId, UserPatchDto userPatchDto) {
        try{
            User user = userRepository.findById(Long.valueOf(userId)).orElse(null);

            if(user == null){
                return APIResponse.genericResponse("failure", 12, "user not found", HttpStatus.NOT_FOUND);
            }
            //Update the user data
            if(userPatchDto.getFirstName() != null){
                user.setFirstName(userPatchDto.getFirstName());
            }
            if(userPatchDto.getLastName() != null){
                user.setLastName(userPatchDto.getLastName());
            }
            if(userPatchDto.getEmail() != null){
                user.setEmail(userPatchDto.getEmail());
            }

            Map<String, Object> updatedUserJson = new HashMap<>();
            updatedUserJson.put("user", user);

            userRepository.save(user);

            return APIResponse.genericResponse("success", 11, updatedUserJson, HttpStatus.OK);
        }catch(Exception ex){
            return APIResponse.genericResponse("failure", 12, ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> disableUserAccount(DisableAccountDto disableAccountDto) {
        return null;
    }

    @Override
    public ResponseEntity<Map<String, Object>> fetchAllUsers() {
        try{
            List<User> users = userRepository.findAll();
            Map<String, Object> userData = new HashMap<>();
            userData.put("users", users);
            userData.put("count", users.size());
            return APIResponse.genericResponse("success", 11, userData, HttpStatus.OK);
        }catch(Exception ex){
            return APIResponse.genericResponse("failure", 12, ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> updateMyUserDetails(UserPatchDto userPatchDto) {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();
            String username = (String) principal;

            User user = userRepository.findByEmail(username).orElse(null);

            if(user == null){
                return APIResponse.genericResponse("failure", 12, "user not found", HttpStatus.NOT_FOUND);
            }
            //Update the user data
            if(userPatchDto.getFirstName() != null){
                user.setFirstName(userPatchDto.getFirstName());
            }
            if(userPatchDto.getLastName() != null){
                user.setLastName(userPatchDto.getLastName());
            }
            if(userPatchDto.getEmail() != null){
                user.setEmail(userPatchDto.getEmail());
            }

            Map<String, Object> updatedUserJson = new HashMap<>();
            updatedUserJson.put("user", user);

            userRepository.save(user);

            return APIResponse.genericResponse("success", 11, updatedUserJson, HttpStatus.OK);
        }catch(Exception ex){
            return APIResponse.genericResponse("failure", 12, ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
