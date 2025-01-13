package com.defty.movie.service.impl;

import com.defty.movie.dto.request.BannerRequest;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.BannerResponse;
import com.defty.movie.dto.response.BannerResponse;
import com.defty.movie.dto.response.PageableResponse;
import com.defty.movie.entity.Banner;
import com.defty.movie.exception.ImageUploadException;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.mapper.BannerMapper;
import com.defty.movie.repository.IBannerRepository;
import com.defty.movie.service.IBannerService;
import com.defty.movie.utils.UploadImageUtil;
import com.defty.movie.validation.BannerValidation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BannerService implements IBannerService {
    BannerMapper bannerMapper;
    BannerValidation bannerValidation;
    IBannerRepository bannerRepository;
    UploadImageUtil uploadImageUtil;
    @Override
    public ApiResponse<Integer> addBanner(BannerRequest bannerRequest) {
        bannerValidation.fieldValidation(bannerRequest);
        Banner bannerEntity = bannerMapper.toBannerEntity(bannerRequest);
        String link = "defty://" + bannerRequest.getContentType() + "?id=" + bannerRequest.getContentId();
        try {
            bannerEntity.setThumnail(uploadImageUtil.upload(bannerRequest.getThumbnail()));
        }
        catch (Exception e){
            throw new ImageUploadException("Could not upload the image, please try again later!");
        }
        try {
            bannerEntity.setLink(link);
            bannerRepository.save(bannerEntity);
        }
        catch (Exception e){
            return new ApiResponse<>(500, e.getMessage(), bannerEntity.getId());
        }
        return new ApiResponse<>(201, "created", bannerEntity.getId());
    }

    @Override
    public ApiResponse<Integer> updateBanner(Integer id, BannerRequest bannerRequest) {
        bannerValidation.fieldValidation(bannerRequest);
        Optional<Banner> bannerEntity = bannerRepository.findById(id);
        String link = "defty://" + bannerRequest.getContentType() + "?id=" + bannerRequest.getContentId();
        if(bannerEntity.isPresent()){
            Banner updatedBanner = bannerEntity.get();
            BeanUtils.copyProperties(bannerRequest, updatedBanner, "id");
            updatedBanner.setLink(link);
            try {
                bannerRepository.save(updatedBanner);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            throw new NotFoundException("Not found exception");

        }
        return new ApiResponse<>(200, "Update banner successfully", id);
    }

    @Override
    public ApiResponse<List<Integer>> deleteBanner(List<Integer> ids) {
        List<Banner> banners = bannerRepository.findAllById(ids);
        if(banners.size() == 0) throw new NotFoundException("Not found exception");
        for(Banner banner : banners){
            banner.setStatus(-1);
        }
        bannerRepository.saveAll(banners);
        if(ids.size() > 1){
            return new ApiResponse<>(200, "Delete banners successfully", ids);
        }
        return new ApiResponse<>(200, "Delete categorie successfully", ids);
    }

    @Override
    public ApiResponse<Integer> changeStatus(Integer id) {
        Optional<Banner> banner = bannerRepository.findById(id);
        if(banner.get() != null){
            String message = "";
            if(banner.get().getStatus() == 0){
                banner.get().setStatus(1);
                message += "Enable banners successfully";
            }
            else{
                banner.get().setStatus(0);
                message += "Disable banners successfully";
            }
            bannerRepository.save(banner.get());
            return new ApiResponse<>(200, message, id);
        }
        else throw new NotFoundException("Not found exception");
    }

    @Override
    public ApiResponse<PageableResponse<BannerResponse>> getAllBanners(Pageable pageable, String title, Integer status) {
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdDate").descending());
        Page<Banner> banners = bannerRepository.findBanners(title, status, sortedPageable);
        List<BannerResponse> bannerResponses = new ArrayList<>();
        if (banners.isEmpty()){
            throw new NotFoundException("Not found exception");
        }
        else {
            for(Banner c : banners){
                BannerResponse bannerResponse = bannerMapper.toBannerResponse(c);
                String[] parts = null;
                String contentType = null;
                String contentId = null;
                if(bannerResponse.getLink() != null){
                    parts = bannerResponse.getLink().split(":|\\?id=");
                    contentType = parts[1];
                    contentId = parts[2];
                    if (contentType.startsWith("//")) {
                        contentType = contentType.substring(2);
                    }
                }
                bannerResponse.setContentType(contentType);
                bannerResponse.setContentId(Integer.parseInt(contentId));
                bannerResponses.add(bannerResponse);
            }

            PageableResponse<BannerResponse> pageableResponse= new PageableResponse<>(bannerResponses, banners.getTotalElements());
            return new ApiResponse<>(200, "OK", pageableResponse);
        }
    }

    @Override
    public Object getBanner(Integer id) {
        Optional<Banner> banner = bannerRepository.findById(id);
        BannerResponse bannerResponse = bannerMapper.toBannerResponse(banner.get());
        String[] parts = null;
        String contentType = null;
        String contentId = null;
        if(bannerResponse.getLink() != null){
            parts = bannerResponse.getLink().split(":|\\?id=");
            contentType = parts[1];
            contentId = parts[2];
            if (contentType.startsWith("//")) {
                contentType = contentType.substring(2);
            }
        }
        bannerResponse.setContentType(contentType);
        bannerResponse.setContentId(Integer.parseInt(contentId));
        if(banner.isPresent()){
            return new ApiResponse<>(200, "OK", bannerResponse);
        }
        return new ApiResponse<>(404, "Banner doesn't exist", null);
    }
}
