package nl.consumergram.consumergramv2.repositories;

import nl.consumergram.consumergramv2.models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
//    Optional<UserProfile> findByIdAndUser_Username(Long id, String username);

    Optional<List<UserProfile>> findByUser_Username(String username);


}
