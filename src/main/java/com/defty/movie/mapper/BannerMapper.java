package com.defty.movie.mapper;

import com.defty.movie.dto.request.BannerRequest;
import com.defty.movie.dto.response.BannerResponse;
import com.defty.movie.dto.response.SubBannerResponse;
import com.defty.movie.entity.Banner;
import com.defty.movie.repository.ICategoryRepository;
import com.defty.movie.repository.IMovieRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import com.defty.movie.entity.*;

import java.util.*;

@Component
@RequiredArgsConstructor
public class BannerMapper {
    private final ModelMapper modelMapper;
    private final ICategoryRepository categoryRepository;
    private final IMovieRepository movieRepository;
    public Banner toBannerEntity(BannerRequest actorRequest){
        return modelMapper.map(actorRequest, Banner.class);
    }
    public BannerResponse toBannerResponse(Banner banner) {
        BannerResponse bannerResponse = modelMapper.map(banner, BannerResponse.class);
        String contentType = banner.getContentType();
        String contentName = null;

        SubBannerResponse subBannerResponse = new SubBannerResponse();
        if ("category".equals(contentType)) {
            contentName = categoryRepository.findById(banner.getContentId())
                    .map(Category::getName)
                    .orElse(null);
            Optional<Category> category = categoryRepository.findById(banner.getContentId());
            if(category.isPresent()){
                subBannerResponse.setDescription(category.get().getDescription());
                subBannerResponse.setNumberOfChild(category.get().getMovieCategories().size());
                bannerResponse.setContentSlug(category.get().getSlug());

//                Map<String, String>
//                Set<MovieCategory> movieCategorySet = category.get().getMovieCategories();
//                List<Category> categories = new ArrayList<>();
//                for(MovieCategory m : movieCategorySet){
//                    categories.add(m.getCategory());
//                }
//                bannerResponse.setBannerItems(Collections.singleton(categories));
            }

        } else if ("movie".equals(contentType)) {
            contentName = movieRepository.findById(banner.getContentId())
                    .map(Movie::getTitle)
                    .orElse(null);
            Optional<Movie> movie = movieRepository.findById(banner.getContentId());
            if(movie.isPresent()){
                subBannerResponse.setDescription(movie.get().getDescription());
                subBannerResponse.setNumberOfChild(movie.get().getEpisodes().size());
                subBannerResponse.setReleaseDate(movie.get().getReleaseDate());
                bannerResponse.setContentSlug(movie.get().getSlug());

                Map<String, String> categoryMap = new HashMap<>();
                Set<MovieCategory> movieCategorySet = movie.get().getMovieCategories();
                for(MovieCategory m : movieCategorySet){
                   categoryMap.put(m.getCategory().getName(), m.getCategory().getSlug());
                }
                bannerResponse.setBannerItems(categoryMap);
            }
        }

        bannerResponse.setSubBannerResponse(subBannerResponse);
        bannerResponse.setContentName(contentName);
        return bannerResponse;
    }

}
