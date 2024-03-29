package nl.consumergram.consumergramv2.services;
import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.EntityNotFoundException;
import nl.consumergram.consumergramv2.dtos.InputBlogpostDto;
import nl.consumergram.consumergramv2.dtos.OutputBlogpostDto;
import nl.consumergram.consumergramv2.models.BlogPost;
import nl.consumergram.consumergramv2.models.User;
import nl.consumergram.consumergramv2.repositories.BlogPostRepository;
import nl.consumergram.consumergramv2.repositories.UserRepository;
import nl.consumergram.consumergramv2.utils.ImageUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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

    public OutputBlogpostDto createBlogPost(InputBlogpostDto inputBlogpostDto) throws IOException {
        BlogPost blogPost = new BlogPost();

        blogPost.setCaption(inputBlogpostDto.getCaption());
        blogPost.setImageData(inputBlogpostDto.getFile().getBytes());
        blogPost.setImageData(ImageUtil.compressImage(inputBlogpostDto.getFile().getBytes()));
        blogPost.setPrice(inputBlogpostDto.getPrice()); // set the price in BlogPost

        blogPost.setCategories(inputBlogpostDto.getCategories());

        blogPost.setYesNoOption(inputBlogpostDto.isYesNoOption());


        if (inputBlogpostDto.getUsername() != null) {
            User user = new User();
            user.setUsername(inputBlogpostDto.getUsername());
            blogPost.setUser(user);
        }
//        Dit word een eigen functie
        blogPostRepository.save(blogPost);
        OutputBlogpostDto outputBlogpostDto = new OutputBlogpostDto();

        outputBlogpostDto.setCaption(blogPost.getCaption());

        outputBlogpostDto.setUsername(blogPost.getUser().getUsername());
        outputBlogpostDto.setId(blogPost.getId());

        outputBlogpostDto.setFileContent(ImageUtil.decompressImage(blogPost.getImageData()));
        outputBlogpostDto.setPrice(blogPost.getPrice());

        outputBlogpostDto.setCategories(blogPost.getCategories());

        outputBlogpostDto.setYesNoOption(blogPost.isYesNoOption());

        return outputBlogpostDto;
    }


    @Transactional
    public OutputBlogpostDto getBlogPost(String username, Long id) {

        BlogPost blogPost = blogPostRepository.findByIdAndUser_Username(id, username)
                .orElseThrow(() -> new EntityNotFoundException("Blog post not found with username " + username + " and id " + id));

        OutputBlogpostDto outputBlogpostDto = new OutputBlogpostDto();

        outputBlogpostDto.setCaption(blogPost.getCaption());

        outputBlogpostDto.setUsername(blogPost.getUser().getUsername());
        outputBlogpostDto.setId(blogPost.getId());
        outputBlogpostDto.setPrice(blogPost.getPrice());
        outputBlogpostDto.setFileContent(ImageUtil.decompressImage(blogPost.getImageData()));
        outputBlogpostDto.setCategories(blogPost.getCategories());
        outputBlogpostDto.setYesNoOption(blogPost.isYesNoOption());
        return outputBlogpostDto;
    }

    @Transactional
    public List<OutputBlogpostDto> getAllBlogs() {
        List<BlogPost> blogPostList = blogPostRepository.findAll();

        List<OutputBlogpostDto> outputBlogpostDtoList = new ArrayList<>();

        for (BlogPost blogPost : blogPostList) {
            OutputBlogpostDto outputBlogpostDto = new OutputBlogpostDto();

            outputBlogpostDto.setCaption(blogPost.getCaption());

            outputBlogpostDto.setCategories(blogPost.getCategories());
            outputBlogpostDto.setYesNoOption(blogPost.isYesNoOption());


            outputBlogpostDto.setFileContent(ImageUtil.decompressImage(blogPost.getImageData()));
            outputBlogpostDto.setUsername(blogPost.getUser().getUsername());
            outputBlogpostDto.setId(blogPost.getId());
            outputBlogpostDto.setPrice(blogPost.getPrice());
            outputBlogpostDtoList.add(outputBlogpostDto);


        }

        return outputBlogpostDtoList;
    }

    @Transactional
    public List<OutputBlogpostDto> getBlogPostByUsername(String username) {
        List<BlogPost> blogPostList = blogPostRepository.findByUser_Username(username)
                .orElseThrow(() -> new EntityNotFoundException("Blog post not found with username " + username));

        List<OutputBlogpostDto> outputBlogpostDtoList = new ArrayList<>();

        for (BlogPost blogPost : blogPostList) {
            OutputBlogpostDto outputBlogpostDto = new OutputBlogpostDto();

            outputBlogpostDto.setCaption(blogPost.getCaption());

            outputBlogpostDto.setUsername(blogPost.getUser().getUsername());
            outputBlogpostDto.setId(blogPost.getId());
            outputBlogpostDto.setPrice(blogPost.getPrice());
            outputBlogpostDto.setFileContent(ImageUtil.decompressImage(blogPost.getImageData()));
            outputBlogpostDtoList.add(outputBlogpostDto);
            outputBlogpostDto.setCategories(blogPost.getCategories());
            outputBlogpostDto.setYesNoOption(blogPost.isYesNoOption());
        }
        ;
        return outputBlogpostDtoList;
    }

    public void deleteBlogPost(String username, Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        BlogPost blogPost = blogPostRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Blog post not found"));
        User user = userRepository.findById(username).orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (user.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            // kijkt of ROLE_ADMIN is en doet verwijderen
            blogPostRepository.delete(blogPost);
        } else if (blogPost.getUser().getUsername().equals(username)) {
            blogPostRepository.delete(blogPost);
        } else {
            // denied
            throw new AccessDeniedException("You are not allowed to delete this blog post");
        }

    }






}