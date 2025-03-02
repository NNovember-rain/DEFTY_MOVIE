package com.defty.movie.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieDetailActorResponse {
    DirectorResponse directorResponse;
    List<ActorResponse> actors;
}
