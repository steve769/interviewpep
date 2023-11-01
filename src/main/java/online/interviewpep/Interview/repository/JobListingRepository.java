package online.interviewpep.Interview.repository;

import online.interviewpep.Interview.entity.JobListing;
import online.interviewpep.Interview.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobListingRepository extends JpaRepository<JobListing, Long> {

    List<JobListing> findAllByCategory(String category);

    List<JobListing> findAllByPoster(User userInContext);
}
