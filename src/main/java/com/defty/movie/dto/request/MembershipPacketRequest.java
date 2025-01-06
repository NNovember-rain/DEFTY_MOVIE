package com.defty.movie.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MembershipPacketRequest {
    String name;
    Integer price;
    Integer discount;
    Integer duration;
    Integer status;
    String description;
}
