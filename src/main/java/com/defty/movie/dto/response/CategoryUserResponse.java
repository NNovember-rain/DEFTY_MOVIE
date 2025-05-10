package com.defty.movie.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryUserResponse {
    List<String> regions;
    List<String> paidCategories;
    List<Integer> releaseDates;
    List<String> categories;
}
