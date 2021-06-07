package com.kaustav.redditbackend.controllers;

import com.kaustav.redditbackend.dto.SubredditDto;
import com.kaustav.redditbackend.models.Subreddit;
import com.kaustav.redditbackend.repositories.SubredditRepository;
import com.kaustav.redditbackend.services.SubredditService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.springframework.http.ResponseEntity.status;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/subreddit")
@AllArgsConstructor
public class SubredditController
{
    private final SubredditService subredditService;

    @GetMapping
    public List<SubredditDto> getAllSubreddits()
    {
        return subredditService.getAll();
    }

    @GetMapping("/{id}")
    public SubredditDto getSubreddit(@PathVariable Long id)
    {
        return subredditService.getSubreddit(id);
    }

    @GetMapping("/created")
    public List<SubredditDto> getCreatedSubreddits()
    {
        return subredditService.getCreatedSubreddits();
    }

    @GetMapping("/subscribed-by-user/{name}")
    public ResponseEntity<List<SubredditDto>> getSubscribedSubredditsByUserName(@PathVariable String name)
    {
        return status(HttpStatus.OK)
                .body(subredditService.getSubscribedSubredditsByUserName(name));
    }

    @PostMapping("/subscribe/{id}")
    public ResponseEntity<Void> subscribe(@PathVariable Long id)
    {
        subredditService.subscribe(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    public SubredditDto create(@RequestBody @Valid SubredditDto subredditDto)
    {
        return subredditService.save(subredditDto);
    }
}