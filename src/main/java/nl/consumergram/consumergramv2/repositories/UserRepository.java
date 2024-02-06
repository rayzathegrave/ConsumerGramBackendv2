package nl.consumergram.consumergramv2.repositories;

import nl.consumergram.consumergramv2.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
