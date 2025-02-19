package com.defty.movie.controller.user;

import com.defty.movie.dto.response.ActorResponse;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.PageableResponse;
import com.defty.movie.entity.Actor;
import com.defty.movie.service.IActorService;
import com.defty.movie.service.impl.ActorService;
import com.defty.movie.utils.ApiResponeUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/user/actor")
public class ActorUserController {
    private final IActorService actorService;

    @GetMapping()
    public Object getAllActorsOrder(@Valid @RequestParam(value = "page", defaultValue = "0") int page,
                                    @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("order"), Sort.Order.desc("createdDate")));
        PageableResponse<ActorResponse> actorsOrderResponse = actorService.getAllActorsOrder(pageable);
        return ApiResponeUtil.ResponseOK(actorsOrderResponse);
    }

}
