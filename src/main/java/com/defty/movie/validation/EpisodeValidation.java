package com.defty.movie.validation;

import com.defty.movie.dto.request.EpisodeRequest;
import com.defty.movie.exception.FieldRequiredException;
import org.springframework.stereotype.Component;

@Component
public class EpisodeValidation {
    public void fieldValidation(EpisodeRequest episodeRequest){
        String message = "";
        if(episodeRequest.getNumber() == null) message += "Episode number can't be left blank!";
        if(episodeRequest.getMovieId() == null) message += "MovieId is null!";
        if(episodeRequest.getNumber() == null || episodeRequest.getMovieId() == null){
            message = "Field Riquired Exception!" + message;
            throw new FieldRequiredException(message);
        }
    }

    boolean checknull(String s){
        if(s == null || s.trim().equals("")) return true;
        return false;
    }
}
