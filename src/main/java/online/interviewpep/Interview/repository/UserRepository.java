package online.interviewpep.Interview.repository;

import online.interviewpep.Interview.entity.User;
import online.interviewpep.Interview.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String username);

    List<User> findAllByRole(Role role);
}
