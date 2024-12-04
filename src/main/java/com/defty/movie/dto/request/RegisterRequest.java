package com.defty.movie.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {
    String username;
    String email;
    String fullName;
    String phone;
    String gender;
    String address;
    LocalDate dateOfBirth;
    String password;
}
