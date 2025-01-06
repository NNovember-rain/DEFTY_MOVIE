package com.defty.movie.service;

import com.defty.movie.dto.request.MembershipPacketRequest;
import com.defty.movie.dto.response.MembershipPacketResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IMembershipPackageService {
    MembershipPacketResponse getMembershipPacketById(Integer id);
    MembershipPacketResponse createMembershipPacket(MembershipPacketRequest membershipPacketRequest);
    void updateMembershipPacket(Integer id, MembershipPacketRequest membershipPacketRequest);
    void deleteMembershipPacket(List<Integer> ids);
    Page<MembershipPacketResponse> getAllMembershipPackets(String search, Pageable pageable);
    Integer switchStatus(Integer id);
}
