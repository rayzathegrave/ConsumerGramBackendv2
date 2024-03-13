package nl.consumergram.consumergramv2.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.consumergram.consumergramv2.dtos.InputBlogpostDto;
import nl.consumergram.consumergramv2.dtos.OutputBlogpostDto;
import nl.consumergram.consumergramv2.models.BlogPost;
import nl.consumergram.consumergramv2.repositories.BlogPostRepository;
import nl.consumergram.consumergramv2.services.BlogPostService;
import nl.consumergram.consumergramv2.utils.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/blog-posts")
public class BlogPostController {

    private final BlogPostService blogPostService;
    private final BlogPostRepository blogPostRepository;

    public BlogPostController(BlogPostService blogPostService, BlogPostRepository blogPostRepository) {
        this.blogPostService = blogPostService;
        this.blogPostRepository = blogPostRepository;
    }


    @GetMapping("/{username}/{id}")
    public ResponseEntity<OutputBlogpostDto> getBlogPost(@PathVariable("username") String username, @PathVariable("id") Long id) {
        OutputBlogpostDto blogPost = blogPostService.getBlogPost(username, id);
        return ResponseEntity.ok(blogPost);
    }


    @GetMapping("/{username}")
    public ResponseEntity<List<OutputBlogpostDto>> getBlogPostByUsername(@PathVariable("username") String username) {
        List<OutputBlogpostDto> blogPost = blogPostService.getBlogPostByUsername(username);
        return ResponseEntity.ok(blogPost);
    }

    @GetMapping
    public ResponseEntity<List<OutputBlogpostDto>> getAllBlogs() {
        List<OutputBlogpostDto> blogPost = blogPostService.getAllBlogs();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(blogPost);
    }

//    @PostMapping("/{username}")
//    public ResponseEntity<OutputBlogpostDto> createBlogPost(@RequestBody InputBlogpostDto blogPost) {
//        OutputBlogpostDto createdPost = blogPostService.createBlogPost(blogPost);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
//    }


    @PostMapping("/{username}")
    public ResponseEntity<OutputBlogpostDto> createBlogPost(@RequestPart("file") MultipartFile file,
                                                            @RequestPart("username") String username,
                                                            @RequestPart("caption") String caption,
                                                            @RequestPart("price") String price,
//                                                          @RequestPart("yesNoOption") boolean yesNoOption,
                                                            @RequestPart("yesNoOption") String yesNoOptionStr,
                                                            @RequestPart("categories") String categoriesJson) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        Set<Category> categories = new HashSet<>();
        Category c1 = Category.valueOf(categoriesJson);
        categories.add(c1);
        boolean yesNoOption = Boolean.parseBoolean(yesNoOptionStr);

        System.out.println("file: " + file);
        System.out.println("username: " + username);
        System.out.println("caption: " + caption);
        System.out.println("price: " + price);
        InputBlogpostDto blogPost = new InputBlogpostDto();
        blogPost.setCaption(caption);
        blogPost.setUsername(username);
        blogPost.setFile(file);
        blogPost.setPrice(price);
        blogPost.setYesNoOption(yesNoOption);
        blogPost.setCategories(categories);
        OutputBlogpostDto createdPost = blogPostService.createBlogPost(blogPost);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }


    @DeleteMapping("/{username}/{id}")
    public ResponseEntity<Void> deleteBlogPost(@PathVariable("username") String username, @PathVariable("id") Long id) {
        blogPostService.deleteBlogPost(username, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/upvotes")
    public ResponseEntity<Integer> getUpvoteCount(@PathVariable Long id) {
        BlogPost blogPost = blogPostRepository.findById(id)
                .orElseThrow(() -> new ExceptionController.ResourceNotFoundException("Blog post not found"));

        return ResponseEntity.ok(blogPost.getUpvotes());
    }
}



