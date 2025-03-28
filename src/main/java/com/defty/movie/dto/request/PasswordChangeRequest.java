package com.defty.movie.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PasswordChangeRequest {

    String oldPassword;
    String newPassword;
    String confirmNewPassword;
}
