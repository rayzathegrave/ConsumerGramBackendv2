package nl.consumergram.consumergramv2.repositories;

import lombok.Data;
import nl.consumergram.consumergramv2.models.BlogPost;
import nl.consumergram.consumergramv2.models.Upvote;
import nl.consumergram.consumergramv2.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UpvoteRepository extends JpaRepository<Upvote, Long> {
    Optional<Upvote> findByUserAndBlogPost(User user, BlogPost blogPost);
}
