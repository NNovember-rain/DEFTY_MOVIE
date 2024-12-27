package com.defty.movie.validation;

import com.defty.movie.dto.request.MovieRequest;
import com.defty.movie.exception.FieldRequiredException;
import org.springframework.stereotype.Component;

@Component
public class MovieValidation {
    public void fieldValidation(MovieRequest movieRequest){
        String message = "";
        if(checknull(movieRequest.getTitle())) message += "Movie name can't be left blank!";
        if(movieRequest.getDirector() == null) message += "DirectorId is null!";
        if(checknull(movieRequest.getTitle()) || movieRequest.getDirector() == null){
            message = "Field Riquired Exception!" + message;
            throw new FieldRequiredException(message);
        }
    }

    boolean checknull(String s){
        if(s == null || s.trim().equals("")) return true;
        return false;
    }
}
