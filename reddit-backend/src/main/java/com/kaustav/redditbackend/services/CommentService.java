package com.kaustav.redditbackend.services;

import com.kaustav.redditbackend.dto.CommentDto;
import com.kaustav.redditbackend.exceptions.PostNotFoundException;
import com.kaustav.redditbackend.exceptions.UsernameNotFoundException;
import com.kaustav.redditbackend.mapper.CommentMapperImpl;
import com.kaustav.redditbackend.models.Comment;
import com.kaustav.redditbackend.models.Post;
import com.kaustav.redditbackend.models.User;
import com.kaustav.redditbackend.repositories.CommentRepository;
import com.kaustav.redditbackend.repositories.PostRepository;
import com.kaustav.redditbackend.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CommentService
{
    PostRepository postRepository;
    CommentRepository commentRepository;
    AuthService authService;
    CommentMapperImpl commentMapper;
    UserRepository userRepository;

    public void createComment(CommentDto commentsDto)
    {
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));

        Comment comment = commentMapper.map(commentsDto, post, authService.getCurrentUser());
        commentRepository.save(comment);
    }

    public List<CommentDto> getCommentsByPost(Long postId)
    {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId.toString()));

        return commentRepository.findByPost(post)
                .stream()
                .map((comment) -> commentMapper.mapToDto(comment))
                .collect(toList());
    }

    public List<CommentDto> getCommentsByUser(String userName)
    {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));

        return commentRepository.findAllByUser(user)
                .stream()
                .map((comment) -> commentMapper.mapToDto(comment))
                .collect(toList());
    }
}