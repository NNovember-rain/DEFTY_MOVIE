package com.defty.movie.validation;

import com.defty.movie.dto.request.CategoryRequest;
import com.defty.movie.exception.FieldRequiredException;
import org.springframework.stereotype.Component;

@Component
public class CategoryValidation {
    public void fieldValidation(CategoryRequest categoryRequest){
        String message = "";
        if(checknull(categoryRequest.getName())){
            message += "Field Riquired Exception!Category name can't be left blank!";
            throw new FieldRequiredException(message);
        }
    }
    boolean checknull(String s){
        if(s == null || s.trim().equals("")) return true;
        return false;
    }
}
