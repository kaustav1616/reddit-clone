package com.kaustav.redditbackend.mapper;

import com.kaustav.redditbackend.dto.SubredditDto;
import com.kaustav.redditbackend.models.Post;
import com.kaustav.redditbackend.models.Subreddit;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubredditMapper
{
    String prefix = "/r/";

    @Mapping(target = "postCount", expression = "java(mapPosts(subreddit.getPosts()))")
    SubredditDto mapSubredditToDto(Subreddit subreddit);

    default Integer mapPosts(List<Post> numberOfPosts)
    {
        return numberOfPosts.size();
    }

    @InheritInverseConfiguration
    @Mapping(target = "name", expression = "java(prefix + subreddit.getName())")
    @Mapping(target = "posts", ignore = true)
    @Mapping(target = "subscribedUsers", ignore = true)
    @Mapping(target = "id", ignore = true)
    Subreddit mapDtoToSubreddit(SubredditDto subreddit);
}