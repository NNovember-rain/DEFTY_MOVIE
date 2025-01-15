package com.defty.movie.validation;

import com.defty.movie.dto.request.BannerRequest;
import com.defty.movie.exception.FieldRequiredException;
import org.springframework.stereotype.Component;

@Component
public class BannerValidation {
    public void fieldValidation(BannerRequest bannerRequest){
        String message = "";
        if(checknull(bannerRequest.getTitle())) message += "Banner title can't be left blank!";
        if(checknull(bannerRequest.getTitle())){
            message = "Field Riquired Exception!" + message;
            throw new FieldRequiredException(message);
        }
    }

    boolean checknull(String s){
        if(s == null || s.trim().equals("")) return true;
        return false;
    }
}
