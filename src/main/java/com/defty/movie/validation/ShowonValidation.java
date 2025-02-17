package com.defty.movie.validation;

import com.defty.movie.dto.request.ActorRequest;
import com.defty.movie.dto.request.ShowonRequest;
import com.defty.movie.exception.FieldRequiredException;
import org.springframework.stereotype.Component;

@Component
public class ShowonValidation {
    public void fieldValidation(ShowonRequest showonRequest){
        String message = "";
        if(showonRequest.getPosition() == null) message += "Position can't be left blank!";
        if(checknull(showonRequest.getContentType())) message += "Content type can't be left blank!";
        if(showonRequest.getContentId() == null) message += "Content id can't be left blank!";
        if(showonRequest.getPosition() == null || checknull(showonRequest.getContentType()) || showonRequest.getContentId() == null){
            message = "Field Riquired Exception!" + message;
            throw new FieldRequiredException(message);
        }
    }

    boolean checknull(String s){
        if(s == null || s.trim().equals("")) return true;
        return false;
    }
}
