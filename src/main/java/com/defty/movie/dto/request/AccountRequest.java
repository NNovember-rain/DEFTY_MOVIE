package com.defty.movie.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountRequest {
    String username;
    String email;
    String fullName;
    String phone;
    String gender;
    String address;
    MultipartFile avatar;
    LocalDate dateOfBirth;
    String password;
    String role;
}
