package com.defty.movie.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponse {
    Integer id;
    String username;
    String email;
    String fullName;
    String phone;
    String gender;
    String address;
    String avatar;
    Integer status;
    LocalDate dateOfBirth;
    String role;
}
