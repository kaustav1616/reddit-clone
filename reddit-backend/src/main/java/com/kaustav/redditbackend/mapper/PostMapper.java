package com.kaustav.redditbackend.mapper;

import com.kaustav.redditbackend.dto.PostRequest;
import com.kaustav.redditbackend.dto.PostResponse;
import com.kaustav.redditbackend.models.Post;
import com.kaustav.redditbackend.models.Subreddit;
import com.kaustav.redditbackend.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper
{
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "postName", source = "postRequest.postName")
    @Mapping(target = "url", source = "postRequest.url")
    @Mapping(target = "voteCount", ignore = true)
    @Mapping(target = "postId", ignore = true)
    @Mapping(target = "votes", ignore = true)
    @Mapping(target = "comments", ignore = true)
    Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.username")
    PostResponse mapToDto(Post post);
}