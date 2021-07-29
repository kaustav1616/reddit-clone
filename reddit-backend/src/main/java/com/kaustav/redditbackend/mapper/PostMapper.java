package com.kaustav.redditbackend.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.kaustav.redditbackend.dto.PostRequest;
import com.kaustav.redditbackend.dto.PostResponse;
import com.kaustav.redditbackend.models.Post;
import com.kaustav.redditbackend.models.Subreddit;
import com.kaustav.redditbackend.models.User;
import com.kaustav.redditbackend.repositories.CommentRepository;
import com.kaustav.redditbackend.repositories.VoteRepository;
import com.kaustav.redditbackend.services.AuthService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class PostMapper
{
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private AuthService authService;

    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "voteCount", constant = "0")
    public abstract Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))") // doing this to do something new; can also do "java(post.getComments().size())"
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    public abstract PostResponse mapToDto(Post post);

    Integer commentCount(Post post)
    {
        return commentRepository.findByPost(post).size();
    }

    String getDuration(Post post)
    {
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }
}