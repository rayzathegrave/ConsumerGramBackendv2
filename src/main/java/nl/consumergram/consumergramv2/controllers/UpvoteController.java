package nl.consumergram.consumergramv2.controllers;

import nl.consumergram.consumergramv2.dtos.InputUpvoteDto;
import nl.consumergram.consumergramv2.dtos.OutputUpvoteDto;
import nl.consumergram.consumergramv2.services.UpvoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/upvotes")
public class UpvoteController {
    private final UpvoteService upvoteService;

    public UpvoteController(UpvoteService upvoteService) {
        this.upvoteService = upvoteService;
    }

    // constructor

    @PostMapping
    public ResponseEntity<OutputUpvoteDto> upvoteBlogPost(@RequestBody InputUpvoteDto inputUpvoteDto) {
        OutputUpvoteDto outputUpvoteDto = upvoteService.upvoteBlogPost(inputUpvoteDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(outputUpvoteDto);
    }
}