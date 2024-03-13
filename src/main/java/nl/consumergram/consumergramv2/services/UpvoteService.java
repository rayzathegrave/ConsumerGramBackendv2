package nl.consumergram.consumergramv2.services;

import lombok.Data;
import nl.consumergram.consumergramv2.controllers.ExceptionController;
import nl.consumergram.consumergramv2.dtos.InputUpvoteDto;
import nl.consumergram.consumergramv2.dtos.OutputUpvoteDto;
import nl.consumergram.consumergramv2.models.BlogPost;
import nl.consumergram.consumergramv2.models.Upvote;
import nl.consumergram.consumergramv2.models.User;
import nl.consumergram.consumergramv2.repositories.BlogPostRepository;
import nl.consumergram.consumergramv2.repositories.UpvoteRepository;
import nl.consumergram.consumergramv2.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Data
@Service
public class UpvoteService {
    private final UpvoteRepository upvoteRepository;
    private final UserRepository userRepository;
    private final BlogPostRepository blogPostRepository;

    // constructor

    public OutputUpvoteDto upvoteBlogPost(InputUpvoteDto inputUpvoteDto) {
        User user = userRepository.findByUsername(inputUpvoteDto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        BlogPost blogPost = blogPostRepository.findById(inputUpvoteDto.getBlogPostId())
                .orElseThrow(() -> new ExceptionController.ResourceNotFoundException("Blog post not found"));

        if (upvoteRepository.findByUserAndBlogPost(user, blogPost).isPresent()) {
            throw new ExceptionController.AlreadyExistsException("User has already upvoted this blog post");
        }

        Upvote upvote = new Upvote();
        upvote.setUser(user);
        upvote.setBlogPost(blogPost);
        upvote = upvoteRepository.save(upvote);

        // Increment the upvotes field of the blog post
        blogPost.incrementUpvotes();
        blogPostRepository.save(blogPost);

        OutputUpvoteDto outputUpvoteDto = new OutputUpvoteDto();
        outputUpvoteDto.setId(upvote.getId());
        outputUpvoteDto.setUsername(user.getUsername());
        outputUpvoteDto.setBlogPostId(blogPost.getId());

        return outputUpvoteDto;
    }
}