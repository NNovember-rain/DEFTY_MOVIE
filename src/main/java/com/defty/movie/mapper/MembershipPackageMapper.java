package com.defty.movie.mapper;

import com.defty.movie.dto.request.MembershipPacketRequest;
import com.defty.movie.dto.response.MembershipPacketResponse;
import com.defty.movie.entity.MembershipPackage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MembershipPackageMapper {
    ModelMapper modelMapper;
    public MembershipPackage toMembershipPacket(MembershipPacketRequest membershipPacketRequest) {
        return modelMapper.map(membershipPacketRequest, MembershipPackage.class);
    }

    public MembershipPacketResponse toMembershipPacketResponse(MembershipPackage membershipPackage) {
        return modelMapper.map(membershipPackage, MembershipPacketResponse.class);
    }
}
