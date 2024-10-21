package com.defty.movie.validation;

import com.defty.movie.dto.request.ActorRequest;
import com.defty.movie.exception.FieldRequiredException;
import org.springframework.stereotype.Component;

@Component
public class ActorValidation{
    public void fieldValidation(ActorRequest actorRequest){
        String message = "";
        if(checknull(actorRequest.getFullName())) message += "Actor full name can't be left blank!";
        if(checknull(actorRequest.getGender())) message += "Actor gender can't be left blank!";
        if(checknull(actorRequest.getFullName()) || checknull(actorRequest.getGender())){
            message = "Field Riquired Exception!" + message;
            throw new FieldRequiredException(message);
        }
    }

    boolean checknull(String s){
        if(s == null || s.trim().equals("")) return true;
        return false;
    }
}
