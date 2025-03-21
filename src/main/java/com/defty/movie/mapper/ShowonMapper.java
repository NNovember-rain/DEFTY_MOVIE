package com.defty.movie.mapper;

import com.defty.movie.dto.request.ShowonRequest;
import com.defty.movie.dto.response.ShowonResponse;
import com.defty.movie.dto.response.SubCategoryResponse;
import com.defty.movie.entity.Category;
import com.defty.movie.entity.Movie;
import com.defty.movie.entity.MovieCategory;
import com.defty.movie.entity.Showon;
import com.defty.movie.repository.ICategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShowonMapper {
    ModelMapper modelMapper;
    ICategoryRepository categoryRepository;
    public Showon toShowonEntity(ShowonRequest showonRequest){
        return modelMapper.map(showonRequest, Showon.class);
    }
    public ShowonResponse toShowonResponse(Showon showon){
        ShowonResponse showonResponse = modelMapper.map(showon, ShowonResponse.class);
        showonResponse.setContentName(showon.getCategory().getName());
        showonResponse.setContentId(showon.getCategory().getId());

        if(showon.getContentType().equals("category")){
            Set<MovieCategory> movieCategories = showon.getCategory().getMovieCategories();
            List<SubCategoryResponse> subCategoryResponses = new ArrayList<>();

            for(MovieCategory m : movieCategories){
                Movie movie = m.getMovie();
                SubCategoryResponse subCategoryResponse = new SubCategoryResponse();
                subCategoryResponse.setMovieTitle(movie.getTitle());
                subCategoryResponse.setMovieThumbnail(movie.getThumbnail());
                subCategoryResponse.setDescription(movie.getDescription());
                subCategoryResponse.setNumberOfChild(movie.getEpisodes().size());
                subCategoryResponse.setReleaseDate(movie.getReleaseDate());

                subCategoryResponses.add(subCategoryResponse);
            }
            showonResponse.setContentItems(Collections.singletonList(subCategoryResponses));
        }
        else if(showon.getContentType().equals("director") || showon.getContentType().equals("actor")){
            if(showon.getContentType().equals("director")){

            }
            else{

            }
        }
        return showonResponse;
    }
}
