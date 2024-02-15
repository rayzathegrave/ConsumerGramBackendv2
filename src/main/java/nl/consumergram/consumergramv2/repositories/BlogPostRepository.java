package nl.consumergram.consumergramv2.repositories;

import nl.consumergram.consumergramv2.models.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    // Define additional methods if needed
}
