package com.kaustav.redditbackend.controllers;

import com.kaustav.redditbackend.dto.CommentDto;
import com.kaustav.redditbackend.services.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/comments/")
@AllArgsConstructor
public class CommentController
{
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentDto commentsDto)
    {
        commentService.createComment(commentsDto);
        return new ResponseEntity<>(CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllCommentsForPost(@RequestParam("postId") Long postId)
    {
        return status(OK)
                .body(commentService.getCommentsByPost(postId));
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllCommentsByUser(@RequestParam("userName") String userName)
    {
        return status(OK).body(commentService.getCommentsByUser(userName));
    }
}