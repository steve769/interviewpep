package online.interviewpep.Interview.service;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.interviewpep.Interview.dto.AddJobListingDto;
import online.interviewpep.Interview.dto.JobListingDto;
import online.interviewpep.Interview.entity.JobListing;
import online.interviewpep.Interview.entity.User;
import online.interviewpep.Interview.repository.JobListingRepository;
import online.interviewpep.Interview.repository.UserRepository;
import online.interviewpep.Interview.utility.APIResponse;
import online.interviewpep.Interview.utility.ValidationFieldResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobListingServiceImpl implements JobListingService{
    public final JobListingRepository jobListingRepository;
    public final UserRepository userRepository;
    @Override
    public ResponseEntity<Map<String, Object>> fetchAllJobListings() {

        try{
            List<JobListing> jobListings = jobListingRepository.findAll();

            if(jobListings.isEmpty()){
                return APIResponse.genericResponse("success", 11, jobListings,HttpStatus.OK);
            }

//            //For active postings
//            if(active.equalsIgnoreCase("true")){
//                List<Object> activeListings = jobListings
//                        .stream()
//                        .filter(l -> l.getDeadline().isAfter(LocalDateTime.now()))
//                        .collect(Collectors.toList());
//                Map<String, Object> listing = new HashMap<>();
//                listing.put("listing", activeListings);
//
//                return genericResponse("success", 11, listing,HttpStatus.OK);
//            }
//
//            //For inactive postings
//            if(active.equalsIgnoreCase("false")){
//                List<Object> inActiveListings = jobListings
//                        .stream()
//                        .filter(l -> l.getDeadline().isBefore(LocalDateTime.now()))
//                        .collect(Collectors.toList());
//
//                Map<String, Object> listing = new HashMap<>();
//                listing.put("listing", inActiveListings);
//
//                return genericResponse("success", 11, listing,HttpStatus.OK);
//            }

            //For all postings

            Map<String, Object> listing = new HashMap<>();
            listing.put("count", jobListings.size());
            listing.put("listing", jobListings);

            return APIResponse.genericResponse("success", 11, listing,HttpStatus.OK);
        }catch(Exception ex){
            return APIResponse.genericResponse("failure",12, ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> createJobListing(AddJobListingDto jobListingDto, BindingResult bindingResult) {
        try{
            //Check for Validation Errors
            if(bindingResult.hasErrors()){
                return ValidationFieldResponse.validationCheckingFailed(bindingResult);
            }

            //Get the User from the Authentication Context
            User userInContext = getUserFromSecurityContext();

            JobListing jobListing = new JobListing();
            jobListing.setTitle(jobListingDto.getTitle());
            jobListing.setCompany(jobListingDto.getCompany());
            jobListing.setAboutCompany(jobListingDto.getAboutCompany());
            jobListing.setDescription(jobListingDto.getDescription());
            jobListing.setCompensation(jobListingDto.getCompensation());
            jobListing.setCategory(jobListingDto.getCategory().toLowerCase());
            jobListing.setPoster(userInContext);

            String [] compensationLimits = jobListingDto.getCompensation().split("-");
            int compensationLowerBound = Integer.valueOf(compensationLimits[0]);
            int compensationUpperBound = Integer.valueOf(compensationLimits[1]);

            int compensationAverage = (compensationLowerBound + compensationUpperBound) /2;

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime formattedDeadline = LocalDateTime.parse(jobListingDto.getDeadline(), dateTimeFormatter);
            
            jobListing.setDeadline(formattedDeadline);
            jobListing.setAverageCompensation(compensationAverage);
            jobListing.setCreatedAt(LocalDateTime.now());

            jobListingRepository.save(jobListing);

            return APIResponse.genericResponse("success", 11, "Listing added successfully", HttpStatus.CREATED);

        }catch(Exception ex){

            return APIResponse.genericResponse("failure",12, ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> getJobListingById(String id) {
        try {
            Long listingId = Long.parseLong(id);
            Optional<JobListing> jobListingOpt = jobListingRepository.findById(listingId);
            JobListing listing = jobListingOpt.orElse(null);

            if(listing == null){
                return APIResponse.genericResponse("failure", 11, "This listing does not exist", HttpStatus.NOT_FOUND);
            }

            Map<String, Object> listingToDisplay = new HashMap<>();
            listingToDisplay.put("listing", listing);

            return APIResponse.genericResponse("success", 11, listingToDisplay, HttpStatus.OK);

        }catch(Exception ex){
            return APIResponse.genericResponse("failure",12, ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> fetchJobListingsByCategory(String category) {
        try{
            List<JobListing> listings = jobListingRepository.findAllByCategory(category);
            Map<String, Object> listingToDisplay = new HashMap<>();
            listingToDisplay.put("listing", listings);
            listingToDisplay.put("count", listings.size());

            return APIResponse.genericResponse("success", 11, listingToDisplay, HttpStatus.OK);
        }catch(Exception ex){
            return APIResponse.genericResponse("failure", 12, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> getAllListingsPostedByMe() {
        try{
            User userInContext = getUserFromSecurityContext();
            List<JobListing> listings = jobListingRepository.findAllByPoster(userInContext);

            Map<String, Object> listingToDisplay = new HashMap<>();
            listingToDisplay.put("listing", listings);
            listingToDisplay.put("count", listings.size());
            listingToDisplay.put("owner", userInContext.getEmail());

            return APIResponse.genericResponse("success", 11, listingToDisplay, HttpStatus.OK);
        }catch(Exception ex){
            return APIResponse.genericResponse("failure", 12, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> getAllListingsByAverageSalary(String min, String max) {
        try{
            List<JobListing> allListings = jobListingRepository.findAll();

            List<JobListing> listings = allListings
                    .stream()
                    .filter(jobListing -> jobListing.getAverageCompensation() >= Integer.parseInt(min) && jobListing.getAverageCompensation() <= Integer.parseInt(max))
                    .collect(Collectors.toList());

            Map<String, Object> listingToDisplay = new HashMap<>();
            listingToDisplay.put("listing", listings);
            listingToDisplay.put("count", listings.size());
            listingToDisplay.put("min", Integer.valueOf(min));
            listingToDisplay.put("max", Integer.valueOf(max));

            return APIResponse.genericResponse("success", 11, listingToDisplay, HttpStatus.OK);
        }catch(Exception ex){
            return APIResponse.genericResponse("failure", 12, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> updateJobListing(String id, JobListingDto jobListingDto) {
        try{
            JobListing jobListing = jobListingRepository.findById(Long.valueOf(id)).orElse(null);

            if(jobListing == null){
                return APIResponse.genericResponse("failure", 12, "Listing not found", HttpStatus.NOT_FOUND);
            }

            if(jobListing.getPoster().getId() != getUserFromSecurityContext().getId() || !getUserFromSecurityContext().getRole().name().equals("ADMIN")){
                return APIResponse.genericResponse("failure", 12, "You cannot update this listing", HttpStatus.FORBIDDEN);
            }

            if(jobListingDto.getTitle() != null){
                jobListing.setTitle(jobListingDto.getTitle());
            }

            if(jobListingDto.getCompany() != null){
                jobListing.setCompany(jobListingDto.getCompany());
            }

            if(jobListingDto.getCategory() != null){
                jobListing.setDescription(jobListingDto.getDescription());
            }

            if(jobListingDto.getAboutCompany() != null){
                jobListing.setAboutCompany(jobListingDto.getAboutCompany());
            }

            if(jobListingDto.getDescription() != null){
                jobListing.setDescription(jobListingDto.getDescription());
            }

            if(jobListingDto.getCompensation() != null){

                //Recalculate Average comp
                String [] compensationLimits = jobListingDto.getCompensation().split("-");
                int compensationLowerBound = Integer.valueOf(compensationLimits[0]);
                int compensationUpperBound = Integer.valueOf(compensationLimits[1]);

                int compensationAverage = (compensationLowerBound + compensationUpperBound) /2;

                jobListing.setCompensation(jobListingDto.getCompensation());
                jobListing.setAverageCompensation(compensationAverage);
            }

            if(jobListingDto.getDeadline() != null){

                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime formattedDeadline = LocalDateTime.parse(jobListingDto.getDeadline(), dateTimeFormatter);

                jobListing.setDeadline(formattedDeadline);
            }
            Map<String, Object> listingToDisplay = new HashMap<>();
            listingToDisplay.put("listing", jobListing);

            jobListingRepository.save(jobListing);

            return APIResponse.genericResponse("success", 11, listingToDisplay, HttpStatus.OK);
        }catch(Exception ex){
            return APIResponse.genericResponse("failure", 12, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> deleteJobListing(String id) {
        try{
            //You can only delete a job posting that you created
            JobListing jobById = jobListingRepository.findById(Long.valueOf(id)).orElse(null);
            User userInContext = getUserFromSecurityContext();
            Long loggedInUserId = userInContext.getId();

            if(jobById == null){
                return APIResponse.genericResponse("failure", 11, "Listing not found", HttpStatus.NOT_FOUND);
            }

            if(!Objects.equals(loggedInUserId, jobById.getPoster().getId()) || !userInContext.getRole().name().equals("ADMIN")){
                return APIResponse.genericResponse("failure", 11, "This is forbidden", HttpStatus.FORBIDDEN);
            }

            jobListingRepository.deleteById(Long.valueOf(id));

            return APIResponse.genericResponse("success", 11, "Listing deleted successfully", HttpStatus.OK);
        }catch(Exception ex){
            return APIResponse.genericResponse("failure", 12, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> applyForJob(String jobId) {
        try{
            //Validation & Job Expiry Check
            JobListing jobListing = jobListingRepository.findById(Long.valueOf(jobId)).orElse(null);
            User userToApply = getUserFromSecurityContext();

            if(jobListing == null){
                return APIResponse.genericResponse("failure", 12, "No such job", HttpStatus.BAD_REQUEST);
            }
            if(userToApply == null){
                return APIResponse.genericResponse("failure", 12, "Action not allowed", HttpStatus.UNAUTHORIZED);
            }
            if(LocalDateTime.now().isAfter(jobListing.getDeadline())){
                return APIResponse.genericResponse("failure", 12, "Job listing is expired", HttpStatus.BAD_REQUEST);
            }
            //if everything is okay then apply for job
            List<JobListing> list = new ArrayList<>();
            list.add(jobListing);

            userToApply.setAppliedJobListings(list);
            userRepository.save(userToApply);

            return APIResponse.genericResponse("success", 11, "Application successful", HttpStatus.OK);
        }catch(Exception ex){
            return APIResponse.genericResponse("failure", 12, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> fetchJobListingByDeadline(String active) {
        try{
            List<JobListing> jobListings = jobListingRepository.findAll();

            if(jobListings.isEmpty()){
                return APIResponse.genericResponse("success", 11, jobListings,HttpStatus.OK);
            }

            //For active postings
            if(active.equalsIgnoreCase("true")){
                List<Object> activeListings = jobListings
                        .stream()
                        .filter(l -> l.getDeadline().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
                Map<String, Object> listing = new HashMap<>();
                listing.put("listing", activeListings);
                listing.put("count", activeListings.size());

                return APIResponse.genericResponse("success", 11, listing,HttpStatus.OK);
            }

            //For inactive postings
            if(active.equalsIgnoreCase("false")){
                List<Object> inActiveListings = jobListings
                        .stream()
                        .filter(l -> l.getDeadline().isBefore(LocalDateTime.now()))
                        .collect(Collectors.toList());

                Map<String, Object> listing = new HashMap<>();
                listing.put("listing", inActiveListings);
                listing.put("count", inActiveListings.size());

                return APIResponse.genericResponse("success", 11, listing,HttpStatus.OK);
            }

            return APIResponse.genericResponse("failure", 11, "Enter either true or false",HttpStatus.BAD_REQUEST);
        }catch(Exception ex){
            return APIResponse.genericResponse("failure",12, ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private User getUserFromSecurityContext(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getPrincipal().toString();
        log.info("THE USERNAME FROM CONTEXT IS "+ username);
        return userRepository.findByEmail(username).orElse(null);
    }
}
