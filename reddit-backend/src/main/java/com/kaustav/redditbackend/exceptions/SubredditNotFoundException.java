package com.kaustav.redditbackend.exceptions;

public class SubredditNotFoundException extends RuntimeException
{
    public SubredditNotFoundException(String message)
    {
        super(message);
    }
}
