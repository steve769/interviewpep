package online.interviewpep.Interview.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import online.interviewpep.Interview.dto.AddJobListingDto;
import online.interviewpep.Interview.dto.JobListingDto;
import online.interviewpep.Interview.service.JobListingService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/listings")
@RequiredArgsConstructor
public class JobController {
    public final JobListingService jobListingService;
    @GetMapping("/getAllListings")
    public ResponseEntity<Map<String, Object>> fetchAllJobListings(){
        return jobListingService.fetchAllJobListings();
    }
    @PostMapping("/createJobListing")
    public ResponseEntity<Map<String, Object>> createJobListing( @Valid @RequestBody AddJobListingDto addJobListingDto, BindingResult bindingResult){

        return jobListingService.createJobListing(addJobListingDto, bindingResult);
    }
    @PatchMapping("/updateJobListing/{id}")
    public ResponseEntity<Map<String, Object>> updateJobListing(@PathVariable String id, @RequestBody JobListingDto jobListingDto){

        return jobListingService.updateJobListing(id, jobListingDto);
    }
    @DeleteMapping("/deleteJobListing/{id}")
    public ResponseEntity<Map<String, Object>> deleteJobListing(@PathVariable String id){

        return jobListingService.deleteJobListing(id);
    }
    @GetMapping("/jobListing/{id}")
    public ResponseEntity<Map<String, Object>> getJobListingById(@PathVariable String id){
        return jobListingService.getJobListingById(id);
    }

    @GetMapping("/getAllListingsByCategory")
    public ResponseEntity<Map<String, Object>> fetchJobListingsByCategory(@RequestParam String category){
        return jobListingService.fetchJobListingsByCategory(category);
    }

    @GetMapping("/getAllListingsPostedByMe")
    public ResponseEntity<Map<String, Object>> getAllListingsPostedByMe(){
        return jobListingService.getAllListingsPostedByMe();
    }

    @GetMapping("/getAllListingsByAverageSalary")
    public ResponseEntity<Map<String, Object>> getAllListingsByAverageSalary(@RequestParam String min,@RequestParam String max){
        return jobListingService.getAllListingsByAverageSalary(min, max);
    }

    @PostMapping("/applyForJob/{jobId}")
    public ResponseEntity<Map<String, Object>> applyForJob(@PathVariable String jobId){
        return jobListingService.applyForJob(jobId);
    }

    @GetMapping("/fetchJobListingByDeadline")
    public ResponseEntity<Map<String, Object>> fetchJobListingByDeadline(@RequestParam String active){
        return jobListingService.fetchJobListingByDeadline(active);
    }
}
