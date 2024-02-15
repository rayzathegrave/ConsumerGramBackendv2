package nl.consumergram.consumergramv2.services;

import jakarta.persistence.EntityNotFoundException;
import nl.consumergram.consumergramv2.models.BlogPost;
import nl.consumergram.consumergramv2.repositories.BlogPostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogPostService {

    private final BlogPostRepository blogPostRepository;

    public BlogPostService(BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
    }

    public List<BlogPost> getAllBlogPosts() {
        return blogPostRepository.findAll();
    }

    public BlogPost createBlogPost(BlogPost blogPost) {
        // Add any additional logic here before saving the blog post
        return blogPostRepository.save(blogPost);
    }



    public BlogPost getBlogPost(Long id) {
        return blogPostRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("BlogPost with id " + id + " not found"));
    }

}