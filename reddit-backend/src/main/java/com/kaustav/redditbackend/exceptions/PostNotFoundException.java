package com.kaustav.redditbackend.exceptions;

public class PostNotFoundException extends RuntimeException
{
    public PostNotFoundException(String message)
    {
        super(message);
    }
}
