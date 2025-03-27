package com.defty.movie.validation;

import com.defty.movie.dto.request.PasswordChangeRequest;
import com.defty.movie.exception.FieldRequiredException;
import org.springframework.stereotype.Component;

@Component
public class ChangePasswordValidation {

    public void fieldValidation(PasswordChangeRequest passwordChangeRequest) {
        if(checknull(passwordChangeRequest.getOldPassword())
        || checknull(passwordChangeRequest.getNewPassword())
        || checknull(passwordChangeRequest.getConfirmNewPassword())) throw new FieldRequiredException("Please fill all the required fields");

        if(!passwordChangeRequest.getNewPassword().equals(passwordChangeRequest.getConfirmNewPassword())){
            throw new FieldRequiredException("Passwords do not match");
        }
    }

    public boolean checknull(String field) {
        if(field == null || field.isEmpty() || field.trim().equals("")) return true;
        return false;
    }
}
