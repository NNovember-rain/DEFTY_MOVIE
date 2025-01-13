package com.defty.movie.service.impl;

import com.defty.movie.dto.request.MembershipPacketRequest;
import com.defty.movie.dto.response.MembershipPacketResponse;
import com.defty.movie.entity.MembershipPackage;
import com.defty.movie.exception.NotFoundException;
import com.defty.movie.mapper.MembershipPackageMapper;
import com.defty.movie.repository.IMembershipPackageRepository;
import com.defty.movie.service.IMembershipPackageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MembershipPackageService implements IMembershipPackageService {
    String PREFIX_MEMBERSHIP_PACKET = "MEMBERSHIP_PACKET | ";
    IMembershipPackageRepository membershipPacketRepository;
    MembershipPackageMapper membershipPackageMapper;

    @Override
    public MembershipPacketResponse getMembershipPacketById(Integer id) {
        Optional<MembershipPackage> membershipPackageOptional = membershipPacketRepository.findById(id);
        if (membershipPackageOptional.isEmpty()){
            log.error(PREFIX_MEMBERSHIP_PACKET + "Membership package not found");
            throw new NotFoundException("Membership package not found");
        }
        MembershipPackage membershipPackage = membershipPackageOptional.get();
        return membershipPackageMapper.toMembershipPacketResponse(membershipPackage);
    }

    @Override
    public MembershipPacketResponse createMembershipPacket(MembershipPacketRequest membershipPacketRequest) {
        MembershipPackage membershipPackage = membershipPackageMapper.toMembershipPacket(membershipPacketRequest);
        membershipPackage.setStatus(1);
        membershipPacketRepository.save(membershipPackage);
        log.info(PREFIX_MEMBERSHIP_PACKET + "Create successfully");
        return membershipPackageMapper.toMembershipPacketResponse(membershipPackage);
    }

    @Override
    public void updateMembershipPacket(Integer id, MembershipPacketRequest membershipPacketRequest) {
        Optional<MembershipPackage> membershipPackageOptional = membershipPacketRepository.findById(id);
        if(membershipPackageOptional.isEmpty()){
            log.error(PREFIX_MEMBERSHIP_PACKET + "Not found");
            throw new NotFoundException("Membership packet not found");
        }
        MembershipPackage membershipPackage = membershipPackageOptional.get();
        membershipPackage.setName(membershipPacketRequest.getName());
        membershipPackage.setDescription(membershipPacketRequest.getDescription());
        membershipPackage.setPrice(membershipPacketRequest.getPrice());
        membershipPackage.setDuration(membershipPacketRequest.getDuration());
        membershipPackage.setDiscount(membershipPacketRequest.getDiscount());
        membershipPacketRepository.save(membershipPackage);
        log.info(PREFIX_MEMBERSHIP_PACKET + "Update successfully");
    }

    @Override
    public void deleteMembershipPacket(List<Integer> ids) {
        List<MembershipPackage> membershipPackages = membershipPacketRepository.findAllById(ids);
        for(MembershipPackage mp : membershipPackages){
            mp.setStatus(-1);
        }
        membershipPacketRepository.saveAll(membershipPackages);
        log.info(PREFIX_MEMBERSHIP_PACKET + "Delete successfully");
    }

    @Override
    public Page<MembershipPacketResponse> getAllMembershipPackets(String search, Pageable pageable) {
        Page<MembershipPackage> membershipPackages;
        if (search != null && !search.isEmpty()) {
            membershipPackages = membershipPacketRepository.findMembershipPackage(search, pageable);
            log.info("{}Get all membership package by name: {}", PREFIX_MEMBERSHIP_PACKET, search);
        } else {
            membershipPackages = membershipPacketRepository.findAllWithStatus(pageable);
            log.info("{}Get all membership package with pageable", PREFIX_MEMBERSHIP_PACKET);
        }
        return membershipPackages.map(membershipPackageMapper::toMembershipPacketResponse);
    }

    @Override
    public Integer switchStatus(Integer id) {
        Optional<MembershipPackage> membershipPackageOptional = membershipPacketRepository.findById(id);
        if(membershipPackageOptional.isEmpty()) {
            log.error("{}Membership package not found id: {}", PREFIX_MEMBERSHIP_PACKET, id);
            throw new NotFoundException("Membership package not found with id: " + id);
        }
        MembershipPackage membershipPackage = membershipPackageOptional.get();
        if (membershipPackage.getStatus() == 1) {
            membershipPackage.setStatus(0);
        }
        else if(membershipPackage.getStatus() == 0) {
            membershipPackage.setStatus(1);
        }
        membershipPacketRepository.save(membershipPackage);
        return membershipPackage.getStatus();
    }
}
