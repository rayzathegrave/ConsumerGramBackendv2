package nl.consumergram.consumergramv2.controllers;

import nl.consumergram.consumergramv2.dtos.InputBlogpostDto;
import nl.consumergram.consumergramv2.dtos.OutputBlogpostDto;
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



    @GetMapping("/{username}/{id}")
    public ResponseEntity<OutputBlogpostDto> getBlogPost(@PathVariable("username") String username, @PathVariable("id") Long id) {
        OutputBlogpostDto blogPost = blogPostService.getBlogPost(username, id);
        return ResponseEntity.ok(blogPost);
    }

    @GetMapping("/{username}")
    public ResponseEntity <List<OutputBlogpostDto>> getBlogPostByUsername(@PathVariable("username") String username) {
        List<OutputBlogpostDto> blogPost = blogPostService.getBlogPostByUsername(username);
        return ResponseEntity.ok(blogPost);
    }

    @GetMapping
    public ResponseEntity<List<OutputBlogpostDto>> getAllBlogs() {
        List<OutputBlogpostDto> blogPost = blogPostService.getAllBlogs();
        return ResponseEntity.ok(blogPost);
    }


    @PostMapping("/{username}")
    public ResponseEntity<OutputBlogpostDto> createBlogPost(@RequestBody InputBlogpostDto blogPost) {
        OutputBlogpostDto createdPost = blogPostService.createBlogPost(blogPost);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }


}
