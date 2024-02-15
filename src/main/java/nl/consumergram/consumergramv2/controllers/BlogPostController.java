package nl.consumergram.consumergramv2.controllers;

import nl.consumergram.consumergramv2.models.BlogPost;
import nl.consumergram.consumergramv2.services.BlogPostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blog-posts")
public class BlogPostController {
    private final BlogPostService blogPostService;

    public BlogPostController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }


//    @GetMapping("/{username}")
//    public ResponseEntity<List<BlogPost>> getAllBlogPosts() {
//        List<BlogPost> blogPosts = blogPostService.getAllBlogPosts();
//        return ResponseEntity.ok(blogPosts);
//    }


    @GetMapping
    public ResponseEntity<List<BlogPost>> getAllBlogPosts() {
        List<BlogPost> blogPosts = blogPostService.getAllBlogPosts();
        return ResponseEntity.ok(blogPosts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogPost> getBlogPost(@PathVariable Long id) {
        BlogPost blogPost = blogPostService.getBlogPost(id);
        return ResponseEntity.ok(blogPost);
    }



    @PostMapping("/{username}")
    public ResponseEntity<BlogPost> createBlogPost(@RequestBody BlogPost blogPost) {
        BlogPost createdPost = blogPostService.createBlogPost(blogPost);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }




}
