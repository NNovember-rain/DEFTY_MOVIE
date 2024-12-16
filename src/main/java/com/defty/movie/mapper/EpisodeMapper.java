package com.defty.movie.mapper;


import com.defty.movie.dto.request.EpisodeRequest;
import com.defty.movie.dto.response.EpisodeResponse;
import com.defty.movie.entity.Episode;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EpisodeMapper {
    private final ModelMapper modelMapper;
    public Episode toEpisodeEntity(EpisodeRequest episodeRequest){
        Episode episode = modelMapper.map(episodeRequest, Episode.class);
        return episode;
    }
    public EpisodeResponse toEpisodeResponseDTO(Episode episode){
        EpisodeResponse episodeResponseDTO = modelMapper.map(episode, EpisodeResponse.class);
        return episodeResponseDTO;
    }
}
