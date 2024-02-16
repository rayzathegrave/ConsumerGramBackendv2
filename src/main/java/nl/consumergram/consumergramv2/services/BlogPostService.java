package nl.consumergram.consumergramv2.services;

import jakarta.persistence.EntityNotFoundException;
import nl.consumergram.consumergramv2.dtos.InputBlogpostDto;
import nl.consumergram.consumergramv2.dtos.OutputBlogpostDto;
import nl.consumergram.consumergramv2.models.BlogPost;
import nl.consumergram.consumergramv2.models.User;
import nl.consumergram.consumergramv2.repositories.BlogPostRepository;
import nl.consumergram.consumergramv2.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BlogPostService {

    private final BlogPostRepository blogPostRepository;
    private final UserRepository userRepository;


    public BlogPostService(BlogPostRepository blogPostRepository, UserRepository userRepository) {
        this.blogPostRepository = blogPostRepository;
        this.userRepository = userRepository;
    }

    public List<BlogPost> getAllBlogPosts(String username, long id) {
        Optional<User> user = userRepository.findById(username);
        if (user.isPresent()) {
            user.get();

        } else {
            throw new EntityNotFoundException("User with username " + username + " not found");
        }
        return blogPostRepository.findAll();
    }

    public OutputBlogpostDto createBlogPost(InputBlogpostDto inputBlogpostDto) {
        BlogPost blogPost = new BlogPost();

        blogPost.setCaption(inputBlogpostDto.getCaption());

        if (inputBlogpostDto.getUsername()!=null) {
            User user = new User();
            user.setUsername(inputBlogpostDto.getUsername());
            blogPost.setUser(user);
        }
//        Dit word een eigen functie
        blogPostRepository.save(blogPost);
        OutputBlogpostDto outputBlogpostDto = new OutputBlogpostDto();

        outputBlogpostDto.setCaption(blogPost.getCaption());
//        outputBlogpostDto.setContent(blogPost.getContent());
        outputBlogpostDto.setUsername(blogPost.getUser().getUsername());
        outputBlogpostDto.setId(blogPost.getId());
        return outputBlogpostDto;
    }


    public OutputBlogpostDto getBlogPost(String username, Long id) {

        BlogPost blogPost = blogPostRepository.findByIdAndUser_Username(id, username)
                .orElseThrow(() -> new EntityNotFoundException("Blog post not found with username " + username + " and id " + id));

        OutputBlogpostDto outputBlogpostDto = new OutputBlogpostDto();
//        outputBlogpostDto.setTitle(blogPost.getTitle());
//        outputBlogpostDto.setSubtitle(blogPost.getSubtitle());
        outputBlogpostDto.setCaption(blogPost.getCaption());
//        outputBlogpostDto.setContent(blogPost.getContent());
        outputBlogpostDto.setUsername(blogPost.getUser().getUsername());
        outputBlogpostDto.setId(blogPost.getId());
        return outputBlogpostDto;
    }

    public List<OutputBlogpostDto> getAllBlogs() {
        List<BlogPost> blogPostList = blogPostRepository.findAll();

        List<OutputBlogpostDto> outputBlogpostDtoList = new ArrayList<>();

        for (BlogPost blogPost : blogPostList) {
            OutputBlogpostDto outputBlogpostDto = new OutputBlogpostDto();
//            outputBlogpostDto.setTitle(blogPost.getTitle());
//            outputBlogpostDto.setSubtitle(blogPost.getSubtitle());
            outputBlogpostDto.setCaption(blogPost.getCaption());
//            outputBlogpostDto.setContent(blogPost.getContent());
            outputBlogpostDto.setUsername(blogPost.getUser().getUsername());
            outputBlogpostDto.setId(blogPost.getId());
            outputBlogpostDtoList.add(outputBlogpostDto);
        };
        return outputBlogpostDtoList;
    }


    public List<OutputBlogpostDto> getBlogPostByUsername(String username) {
        List<BlogPost> blogPostList = blogPostRepository.findByUser_Username(username)
                .orElseThrow(() -> new EntityNotFoundException("Blog post not found with username " + username));

        List<OutputBlogpostDto> outputBlogpostDtoList = new ArrayList<>();

        for (BlogPost blogPost : blogPostList) {
            OutputBlogpostDto outputBlogpostDto = new OutputBlogpostDto();
//            outputBlogpostDto.setTitle(blogPost.getTitle());
//            outputBlogpostDto.setSubtitle(blogPost.getSubtitle());
            outputBlogpostDto.setCaption(blogPost.getCaption());
//            outputBlogpostDto.setContent(blogPost.getContent());
            outputBlogpostDto.setUsername(blogPost.getUser().getUsername());
            outputBlogpostDto.setId(blogPost.getId());
            outputBlogpostDtoList.add(outputBlogpostDto);
        };
        return outputBlogpostDtoList;
    }
}