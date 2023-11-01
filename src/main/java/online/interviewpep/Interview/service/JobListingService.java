package online.interviewpep.Interview.service;

import online.interviewpep.Interview.dto.AddJobListingDto;
import online.interviewpep.Interview.dto.JobListingDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Map;


@Service
public interface JobListingService {
    ResponseEntity<Map<String, Object>> fetchAllJobListings();

    ResponseEntity<Map<String, Object>> createJobListing(AddJobListingDto addJobListingDto, BindingResult bindingResult);

    ResponseEntity<Map<String, Object>> getJobListingById(String id);

    ResponseEntity<Map<String, Object>> fetchJobListingsByCategory(String category);

    ResponseEntity<Map<String, Object>> getAllListingsPostedByMe();

    ResponseEntity<Map<String, Object>> getAllListingsByAverageSalary(String min, String max);

    ResponseEntity<Map<String, Object>> updateJobListing(String id, JobListingDto jobListingDto);

    ResponseEntity<Map<String, Object>> deleteJobListing(String id);

    ResponseEntity<Map<String, Object>> applyForJob(String jobId);

    ResponseEntity<Map<String, Object>> fetchJobListingByDeadline(String active);
}
