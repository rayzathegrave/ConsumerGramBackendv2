package nl.consumergram.consumergramv2.repositories;

import nl.consumergram.consumergramv2.models.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    Optional<BlogPost> findByIdAndUser_Username(Long id, String username);

    Optional<List<BlogPost>> findByUser_Username(String username);


}
