package com.defty.movie.validation;

import com.defty.movie.dto.request.CollectionRequest;
import com.defty.movie.exception.FieldRequiredException;
import org.springframework.stereotype.Component;

@Component
public class CollectionValidation {
    public void fieldValidation(CollectionRequest collectionRequest){
        String message = "";
        if(checknull(collectionRequest.getName())){
            message += "Field Riquired Exception!\nCollection name can't be left blank!";
            throw new FieldRequiredException(message);
        }
    }
    boolean checknull(String s){
        if(s == null || s.trim().equals("")) return true;
        return false;
    }
}
