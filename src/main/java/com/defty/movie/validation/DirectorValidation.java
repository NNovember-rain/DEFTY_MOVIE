package com.defty.movie.validation;

import com.defty.movie.dto.request.DirectorRequest;
import com.defty.movie.exception.FieldRequiredException;
import org.springframework.stereotype.Component;

@Component
public class DirectorValidation {
    public void fieldValidation(DirectorRequest directorRequest){
        String message = "";
        if(checknull(directorRequest.getFullName())) message += "Director full name can't be left blank!";
        if(checknull(directorRequest.getGender())) message += "Director gender can't be left blank!";
        if(checknull(directorRequest.getFullName()) || checknull(directorRequest.getGender())){
            message = "Field Riquired Exception!" + message;
            throw new FieldRequiredException(message);
        }
    }

    boolean checknull(String s){
        if(s == null || s.trim().equals("")) return true;
        return false;
    }
}
