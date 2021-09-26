package com.kaustav.redditbackend.controllers;

import com.kaustav.redditbackend.dto.VoteDto;
import com.kaustav.redditbackend.services.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/votes")
@AllArgsConstructor
//@CrossOrigin
public class VoteController
{
    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<Void> vote(@RequestBody VoteDto voteDto)
    {
        voteService.vote(voteDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}