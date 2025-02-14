package com.defty.movie.controller.admin;

import com.defty.movie.dto.request.MembershipPacketRequest;
import com.defty.movie.dto.response.ApiResponse;
import com.defty.movie.dto.response.MembershipPacketResponse;
import com.defty.movie.service.IMembershipPackageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/admin/membership-package")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MembershipPacketController {
    String PREFIX_MEMBERSHIP_PACKET = "MEMBERSHIP_PACKET | ";
    IMembershipPackageService membershipPackageService;

    @GetMapping("")
    @PreAuthorize("@requiredPermission.checkPermission('GET_ALL_MEMBERSHIP_PACKETS')")
    public ResponseEntity<?> getPermissions(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                            @RequestParam(value = "size", defaultValue = "10") Integer size,
                                            @RequestParam(value = "name", required = false) String name) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<MembershipPacketResponse> membershipPacketResponses = membershipPackageService.getAllMembershipPackets(name, pageable);
        log.info(PREFIX_MEMBERSHIP_PACKET + "Get all membership packet success");
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(membershipPacketResponses)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('GET_MEMBERSHIP_PACKET')")
    public ResponseEntity<?> getPermissionById(@PathVariable Integer id) {
        MembershipPacketResponse membershipPacketResponse = membershipPackageService.getMembershipPacketById(id);
        log.info(PREFIX_MEMBERSHIP_PACKET + "Get membership packet by id success");
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(membershipPacketResponse)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("")
    @PreAuthorize("@requiredPermission.checkPermission('CREATE_MEMBERSHIP_PACKET')")
    public ResponseEntity<?> createMembershipPacket(@RequestBody MembershipPacketRequest membershipPacketRequest) {
        MembershipPacketResponse membershipPacketResponse = membershipPackageService.createMembershipPacket(membershipPacketRequest);
        log.info(PREFIX_MEMBERSHIP_PACKET + "Create membership packet success");
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data(membershipPacketResponse.getId())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{membershipPacketId}")
    @PreAuthorize("@requiredPermission.checkPermission('UPDATE_MEMBERSHIP_PACKET')")
    public ResponseEntity<?> updateMembershipPacket(@PathVariable("membershipPacketId") Integer membershipPacketId,
                                              @RequestBody MembershipPacketRequest membershipPacketRequest) {
        membershipPackageService.updateMembershipPacket(membershipPacketId, membershipPacketRequest);
        log.info(PREFIX_MEMBERSHIP_PACKET + "Update membership packet success");
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data("success")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{membershipPacketIds}")
    @PreAuthorize("@requiredPermission.checkPermission('DELETE_MEMBERSHIP_PACKETS')")
    public ResponseEntity<?> deleteMembershipPackets(@PathVariable List<Integer> membershipPacketIds) {
        membershipPackageService.deleteMembershipPacket(membershipPacketIds);
        log.info(PREFIX_MEMBERSHIP_PACKET + "Delete membership packet success");
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data("success")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/status/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('STATUS_MEMBERSHIP_PACKET')")
    public ResponseEntity<?> switchStatus(@PathVariable("id") Integer id) {
        Integer status = membershipPackageService.switchStatus(id);
        log.info(PREFIX_MEMBERSHIP_PACKET + "Switch status membership package success");
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(status)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
